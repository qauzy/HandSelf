// Copyright (c) 2019 PaddlePaddle Authors. All Rights Reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
#include <android/asset_manager_jni.h>
#include <android/native_window_jni.h>
#include <android/native_window.h>

#include <android/log.h>

#include <jni.h>
#include <sstream>
#include <thread>
#include <sys/time.h>
#include <time.h>
#include <cmath>
#include <iostream>
#include <string>
#include <vector>
#include<mutex>
#include<map>
#include <time.h>
#include <opencv2/opencv.hpp>


#include "paddle_api.h"  // NOLINT
/////////////////////////////////////////////////////////////////////////
// If this demo is linked to static library:libpaddle_api_light_bundled.a
// , you should include `paddle_use_ops.h` and `paddle_use_kernels.h` to
// avoid linking errors such as `unsupport ops or kernels`.
/////////////////////////////////////////////////////////////////////////
// #include "paddle_use_kernels.h"  // NOLINT
// #include "paddle_use_ops.h"      // NOLINT

using namespace paddle::lite_api;  // NOLINT
using namespace cv;

#if x86
#include <array>

template<class T, size_t N = 4>
struct TxN
{
    T val[N];

    TxN() = default;

    T& operator[](size_t i)
    {
        return val[i];
    }

    T const& operator[](size_t i) const
    {
        return val[i];
    }

    size_t size() const
    {
        return N;
    }

    // there are at least three different ways to do this operator, but
    //  this is the easiest, so I included below.
    friend std::ostream& operator <<(std::ostream& os, TxN<T,N> const& t)
    {
        os << t.val[0];
        for (size_t i=1; i<N; ++i)
            os << ',' << t.val[i];
        return os;
    }
};

// now creating TypeN shrouds is trivial.

using float32x4_t = TxN<float, 4>;
using int16x8_t = TxN<short,8>;
using float32x4x3_t = TxN<float32x4_t, 3>;

static inline float32x4x3_t vld3q_f32(const float *a)
{
    float32x4x3_t r;
    for(int i=0; i<3; i++){
        float32x4_t t;
        for (int j=0; j<4; j++) {
            t[j] = a[i*j];
        }
        r[i] = t;

    }

    return r;
}

/// @ingroup add
/// r[i] = a[i] + b[i]
static inline float32x4_t vaddq_f32(float32x4_t a, float32x4_t b)
{
    float32x4_t r;
    for (int i=0; i<4; i++) {
        r[i] = a[i] + b[i];
    }
    return r;
}

static inline float32x4_t vsubq_f32(float32x4_t a, float32x4_t b)
{
    float32x4_t r;
    for (int i=0; i<4; i++) {
        r[i] = a[i] - b[i];
    }
    return r;
}

static inline float32x4_t vmulq_f32(float32x4_t a, float32x4_t b)
{
    float32x4_t r;
    for (int i=0; i<4; i++) {
        r[i] = a[i] * b[i];
    }
    return r;
}
static inline void vst1q_f32(float *a, float32x4_t& b)
{

    for (int i=0; i<4; i++) {
        a[i] =  b[i];
    }
    return;
}
static inline float32x4_t vdupq_n_f32(const float a)
{
    float32x4_t r;
    for (int i=0; i<4; i++) {
        r[i] =  a;
    }
    return r;
}


#endif
JavaVM *gJavaVM;//全局JavaVM 变量
jobject gJavaObj;//全局Jobject变量
JNIEnv * gEnv;	//全局的JNIEnv变量
jmethodID nativeCallback;//全局的方法ID


struct Object {
  std::string class_name;
  Scalar fill_color;
  float prob;
  int x;
  int y;
  int w;
  int h;
};
static std::string getCurrentTime() {
  auto now   = std::chrono::system_clock::now();
  auto now_ms = std::chrono::duration_cast<std::chrono::milliseconds>(now.time_since_epoch());
  auto sectime   = std::chrono::duration_cast<std::chrono::seconds>(now_ms);

  std::time_t timet = sectime.count();
  struct tm curtime;
  localtime_r(&timet, &curtime);

  char buffer[64];
  sprintf(buffer, "%4d%02d%02d_%02d.%02d.%02d", curtime.tm_year + 1900, curtime.tm_mon + 1,
          curtime.tm_mday, curtime.tm_hour, curtime.tm_min, curtime.tm_sec);
  return std::string(buffer);
}

// fill tensor with mean and scale and trans layout: nhwc -> nchw, neon speed up
void neon_mean_scale(const float *din, float *dout, int size, float *mean,
                     float *scale) {
  float32x4_t vmean0 = vdupq_n_f32(mean[0]);
  float32x4_t vmean1 = vdupq_n_f32(mean[1]);
  float32x4_t vmean2 = vdupq_n_f32(mean[2]);
  float32x4_t vscale0 = vdupq_n_f32(1.f / scale[0]);
  float32x4_t vscale1 = vdupq_n_f32(1.f / scale[1]);
  float32x4_t vscale2 = vdupq_n_f32(1.f / scale[2]);

  float *dout_c0 = dout;
  float *dout_c1 = dout + size;
  float *dout_c2 = dout + size * 2;

  int i = 0;
  for (; i < size - 3; i += 4) {
    float32x4x3_t vin3 = vld3q_f32(din);
    float32x4_t vsub0 = vsubq_f32(vin3.val[0], vmean0);
    float32x4_t vsub1 = vsubq_f32(vin3.val[1], vmean1);
    float32x4_t vsub2 = vsubq_f32(vin3.val[2], vmean2);
    float32x4_t vs0 = vmulq_f32(vsub0, vscale0);
    float32x4_t vs1 = vmulq_f32(vsub1, vscale1);
    float32x4_t vs2 = vmulq_f32(vsub2, vscale2);
    vst1q_f32(dout_c0, vs0);
    vst1q_f32(dout_c1, vs1);
    vst1q_f32(dout_c2, vs2);

    din += 12;
    dout_c0 += 4;
    dout_c1 += 4;
    dout_c2 += 4;
  }
  for (; i < size; i++) {
    *(dout_c0++) = (*(din++) - mean[0]) / scale[0];
    *(dout_c1++) = (*(din++) - mean[1]) / scale[1];
    *(dout_c2++) = (*(din++) - mean[2]) / scale[2];
  }
}

void pre_process(std::shared_ptr<PaddlePredictor> predictor, const cv::Mat img,
                 int width, int height) {
  // Prepare scale data from image
  std::unique_ptr<Tensor> input_tensor_scale(std::move(predictor->GetInput(1)));
  input_tensor_scale->Resize({1, 2});
  auto *scale_data = input_tensor_scale->mutable_data<float>();
  scale_data[0] = static_cast<float>(height) / static_cast<float>(img.rows);
  scale_data[1] = static_cast<float>(width) / static_cast<float>(img.cols);

  // Prepare input data from image
  std::unique_ptr<Tensor> input_tensor(std::move(predictor->GetInput(0)));
  input_tensor->Resize({1, 3, height, width});
  float means[3] = {0.485,0.456,0.406};
  float scales[3] = {0.229, 0.224,0.225};
  cv::Mat rgb_img;
  cv::cvtColor(img, rgb_img, cv::COLOR_BGR2RGB);
  cv::resize(rgb_img, rgb_img, cv::Size(width, height), 0.f, 0.f);
  cv::Mat imgf;
  rgb_img.convertTo(imgf, CV_32FC3, 1 / 255.f);
  const float *dimg = reinterpret_cast<const float *>(imgf.data);
  auto *data = input_tensor->mutable_data<float>();
  neon_mean_scale(dimg, data, width * height, means, scales);
}

void Postprocess(std::shared_ptr<PaddlePredictor> predictor_,std::vector<Object> *results) {
  // 5. 获取输出数据
  auto outputTensor = predictor_->GetOutput(0);
  auto output_bbox_tensor = predictor_->GetOutput(1);
  auto outputData = outputTensor->data<float>();
  auto *bbox_num = output_bbox_tensor->data<int>();
  auto outputShape = outputTensor->shape();
//  int outputSize = ShapeProduction(outputShape);

  for (int i = 0; i < bbox_num[0]; i++) {
    // Class id
    auto class_id = static_cast<int>(round(outputData[i * 6]));
    // Confidence score
    auto score = outputData[1 + i * 6];
    int xmin = static_cast<int>(outputData[2 + i * 6]);
    int ymin = static_cast<int>(outputData[3 + i * 6]);
    int xmax = static_cast<int>(outputData[4 + i * 6]);
    int ymax = static_cast<int>(outputData[5 + i * 6]);
    int w = xmax - xmin;
    int h = ymax - ymin;
    if (score < 0.6)
      continue;

    Object object;
    object.class_name = "";
    object.fill_color =  cv::Scalar(0, 0, 0);
    object.prob = score;
    object.x = xmin;
    object.y = ymin;
    object.w = w;
    object.h = h;
    results->push_back(object);
  }
}

void VisualizeResults(const std::vector<Object> &results,
                 cv::Mat *rgbaImage) {
  int oriw = rgbaImage->cols;
  int orih = rgbaImage->rows;
  for (int i = 0; i < results.size(); i++) {
    Object object = results[i];
    cv::Rect boundingBox = cv::Rect(object.x, object.y, object.w, object.h) &
                           cv::Rect(0, 0, oriw - 1, orih - 1);
    // Configure text size
    std::string text = object.class_name + ": ";
    std::string str_prob = std::to_string(object.prob);
    text += str_prob.substr(0, str_prob.find(".") + 4);
    int fontFace = cv::FONT_HERSHEY_PLAIN;
    double fontScale = 1.5f;
    float fontThickness = 1.0f;
    cv::Size textSize =
        cv::getTextSize(text, fontFace, fontScale, fontThickness, nullptr);
    // Draw roi object, text, and background
    cv::rectangle(*rgbaImage, boundingBox, object.fill_color, 2);
    cv::rectangle(*rgbaImage,
                  cv::Point2d(boundingBox.x,
                              boundingBox.y - round(textSize.height * 1.25f)),
                  cv::Point2d(boundingBox.x + boundingBox.width, boundingBox.y),
                  object.fill_color, -1);
    cv::putText(*rgbaImage, text, cv::Point2d(boundingBox.x, boundingBox.y),
                fontFace, fontScale, cv::Scalar(255, 255, 255), fontThickness);
  }
}
Mat gImg;
std::mutex some_mutex;
void GetLive() {
  cv::VideoCapture cap("rtsp://test:123456@192.168.199.173:554/stream2&channel=1"); //视频捕捉对象
  Mat img;
  while (true) {
    //如果没打开重新打开
    if (!cap.isOpened())
    {
      cap.open("rtsp://test:123456@192.168.199.173:554/stream2&channel=1");
      continue ;
    }
    cap >> img;

    std::lock_guard<std::mutex>guard(some_mutex);
    gImg = img.clone();
    int iloop = 15;
    while(iloop>0){
      cap.grab();   //获取下一帧，只取帧不解码
      iloop = iloop - 1;
    }
  }
}


void RunModel(std::string model_dir) {



  // 1. Set MobileConfig
  MobileConfig config;
  // 2. Set the path to the model generated by opt tools
  config.set_model_from_file(model_dir);
  config.set_threads(8);
  config.set_power_mode(static_cast<paddle::lite_api::PowerMode>(0));
  // 3. Create PaddlePredictor by MobileConfig
  std::shared_ptr<PaddlePredictor> predictor =
      CreatePaddlePredictor<MobileConfig>(config);

//
//  std::string metal_lib_path = "../../../metal/lite.metallib";
//  config.set_metal_lib_path(metal_lib_path);
//  config.set_metal_use_mps(true);

  std::unique_ptr<Tensor> input_tensor(std::move(predictor->GetInput(0)));
  input_tensor->Resize({1, 3, 320, 320});
  auto* data = input_tensor->mutable_data<float>();
  int meter[10]={0};
  int cnt = 0;  //检测计数

  int cur = 0;
  int snt = -1;
  JNIEnv *env;
  //从全局的JavaVM中获取到环境变量
  gJavaVM->AttachCurrentThread(&env,NULL);
  while (true) {

    if(gImg.empty()){
      continue ;
    }
    cnt++;
    Mat imgDec;
    std::unique_lock<std::mutex> lk(some_mutex);   //这里使用unique_lock是为了后面方便解锁
    imgDec = gImg.clone();
    lk.unlock();

    std::vector<Object> results;
    pre_process(predictor,imgDec,320,320);
    // 4. Run predictor
    predictor->Run();
    Postprocess(predictor,&results);

//      for (std::vector<Object>::size_type ind = 0;ind != results.size(); ++ind) {
//          std::cout << "idx:" << ind << " x:"<< results[ind].x << " y:" <<results[ind].y << std::endl; // print current element
////          if(results[ind].y < 100)
//      }

    __android_log_print(ANDROID_LOG_DEBUG, "mynahai", "RunModel:%d", (int)results.size());

    meter[cnt%3] = results.size();
    if(cnt%3 == 0){
      int max = 0;  //n次检测确定次数
      std::map<int, int> meterMap;
      for(int i=0;i<3;i++){
        meterMap[meter[i]]++;
        if(meterMap[meter[i]]>max){
          max = meterMap[meter[i]];
          cur = meter[i];
        }
      }
      if(snt != cur){
        snt = cur;
        env->CallVoidMethod(gJavaObj,nativeCallback,snt);

      }
//      if(snt>=6){
//        std::vector <int> compression_params;
//        compression_params.push_back(cv::IMWRITE_JPEG_QUALITY);
//        compression_params.push_back(40);
//        cv::imwrite("/Users/gauss/Documents/20221006/TL-IPC55A_8E-8D_"+getCurrentTime()+"_"+std::to_string(snt)+".jpeg",imgDec,compression_params);
//      }
    }

//    VisualizeResults(results,&imgDec);
//
//    imshow("Image", imgDec);
//    waitKey(1);
  }


}

extern "C" {
// public native boolean openCamera(int facing);
JNIEXPORT jboolean JNICALL Java_cn_iyunbei_handself_service_MynahAI_loadModel(JNIEnv* env, jobject thiz, jstring jname)
{

  gJavaObj = env->NewGlobalRef(thiz);//创建全局引用
  jclass clazz = env->GetObjectClass(thiz);
  nativeCallback = env->GetMethodID(clazz,"onScanResult","(I)V");
  //操作方式二，调用JNI函数保存JavaVM
  env->GetJavaVM(&gJavaVM);

  // 得到传入的UTF-8编码的字符串
  const char* name = env->GetStringUTFChars(jname, nullptr);
  if (name == nullptr) {
    return false;
  }

  __android_log_print(ANDROID_LOG_DEBUG, "mynahai", "mode:%s", name);
  // 构造需要返回的结果字符串
  std::string mode("");
  mode += name;
  // 使用完从GetStringUTFChars的字符串后，需要通知JVM
  env->ReleaseStringUTFChars(jname, name);



  std::thread th(GetLive);
  th.detach();

  RunModel( mode);

  return JNI_TRUE;
}


//Java调用System.loadLibrary()加载一个库的时候，会首先在库中搜索JNI_OnLoad()函数，如果该函数存在，则执行它；
JNIEXPORT jint JNI_OnLoad(JavaVM* vm, void* reserved)
{
    __android_log_print(ANDROID_LOG_DEBUG, "mynahai", "JNI_OnLoad");



    return JNI_VERSION_1_4;
}

//根据JNI文档的描述，当GC回收了加载这个库的ClassLoader时，该函数被调用
JNIEXPORT void JNI_OnUnload(JavaVM* vm, void* reserved)
{
    __android_log_print(ANDROID_LOG_DEBUG, "mynahai", "JNI_OnUnload");

}
}
