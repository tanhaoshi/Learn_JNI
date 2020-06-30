# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.4.1)

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.
#设置生成的so动态库最后输出的路径 最终会输出到我们的常见的jnilibs当中
SET(CMAKE_LIBRARY_OUTPUT_DIRECTORY ${PROJECT_SOURCE_DIR}/../JniLibs/${ANDROID_ABI})

#源工程相关构建脚本
add_library( # Sets the name of the library.
        native-lib

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        native-lib.cpp)

#新增构建脚本,这是构建我们自己的so文件 其路径是cmakelist.txt所在路径，其中TestLibInput.cpp是我自己创建的
#include_directories(${CMAKE_SOURCE_DIR})
#add_library(
#        TestLib
 #       SHARED
 #       TestLibInput.cpp)
# Searches for a specified prebuilt library and stores the path as a
# variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.

#查找系统库log 并将其赋值于log-lib
find_library( # Sets the name of the path variable.
        log-lib

        # Specifies the name of the NDK library that
        # you want CMake to locate.
        log)

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.

#源工程，即刚创建项目时自带的库
target_link_libraries( # Specifies the target library.
        native-lib

        # Links the target library to the log library
        # included in the NDK.
        ${log-lib})

#我们要构建的库,注意依赖问题。
#target_link_libraries(
 #       TestLibInput
 #       ${log-lib})