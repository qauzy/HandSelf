project(mynahai)

cmake_minimum_required(VERSION 3.10)


# 1. link Paddle-Lite directory
link_directories("${CMAKE_SOURCE_DIR}/../libs/${ANDROID_ABI}")
include_directories("${CMAKE_SOURCE_DIR}/include")


# 2. compile options
add_definitions(-std=c++11 -O3 -pthread)
set(EXECUTABLE_OUTPUT_PATH ./)

set(OpenCV_DIR ${CMAKE_SOURCE_DIR}/opencv-4.x/sdk/native/jni)
find_package(OpenCV REQUIRED)


add_library(mynahai SHARED mynahai.cc)


target_link_libraries(mynahai  ${OpenCV_LIBS} mediandk log paddle_light_api_shared -ldl)
