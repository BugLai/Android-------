LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := packer
LOCAL_SRC_FILES := packer.cpp
LOCAL_LDLIBS += -L$(SYSROOT)/usr/lib -llog
include $(BUILD_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_PRELINK_MODULE := false
LOCAL_MODULE := iaes
LOCAL_SRC_FILES := myaes.cpp
LOCAL_CFLAGS = -Wno-psabi
#LOCAL_C_INCLUDES += /usr/include/c++/4.4 /usr/include/c++/4.4/i686-linux-gnu /usr/include/ /usr/include/i386-linux-gnu
include $(BUILD_SHARED_LIBRARY)