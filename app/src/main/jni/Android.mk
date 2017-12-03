LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
LOCAL_MODULE := logicjni
LOCAL_SRC_FILES := logicjni.c
include $(BUILD_SHARED_LIBRARY)