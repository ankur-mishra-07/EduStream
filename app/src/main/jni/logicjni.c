#include <jni.h>
#include "driver_mcrypt.h"

JNIEXPORT jint JNICALL
Java_com_pace_edustream_activity_MainActivity_jvEncryptFileWithKey(JNIEnv *env, jobject instance,
                                                              jstring actual_file_path_,
                                                              jstring encrypted_file_path_,
                                                              jstring key_path_) {
    const char *actual_file_path = (*env)->GetStringUTFChars(env, actual_file_path_, 0);
    const char *encrypted_file_path = (*env)->GetStringUTFChars(env, encrypted_file_path_, 0);
    const char *key_path = (*env)->GetStringUTFChars(env, key_path_, 0);

    // TODO
    int result = 0;
    if (encryptFileWithKey(actual_file_path, encrypted_file_path, key_path) == 1) {
        result = 1;
    }

    (*env)->ReleaseStringUTFChars(env, actual_file_path_, actual_file_path);
    (*env)->ReleaseStringUTFChars(env, encrypted_file_path_, encrypted_file_path);
    (*env)->ReleaseStringUTFChars(env, key_path_, key_path);
    return result;
}


JNIEXPORT jint JNICALL
Java_com_pace_edustream_activity_MainActivity_jvDecryptFileWithKey(JNIEnv *env, jobject instance,
                                                              jstring encrypted_file_path_,
                                                              jstring decrypted_file_path_,
                                                              jstring key_path_) {
    const char *encrypted_file_path = (*env)->GetStringUTFChars(env, encrypted_file_path_, 0);
    const char *decrypted_file_path = (*env)->GetStringUTFChars(env, decrypted_file_path_, 0);
    const char *key_path = (*env)->GetStringUTFChars(env, key_path_, 0);

    // TODO

    int result = 0;
    if (decryptFileWithKey(encrypted_file_path, decrypted_file_path, key_path) == 1) {
        result = 1;
    }

    (*env)->ReleaseStringUTFChars(env, encrypted_file_path_, encrypted_file_path);
    (*env)->ReleaseStringUTFChars(env, decrypted_file_path_, decrypted_file_path);
    (*env)->ReleaseStringUTFChars(env, key_path_, key_path);
    return result;

}

/*
JNIEXPORT jint JNICALL
Java_com_eazeeplay_activity_MainActivity_jvEncryptFileWithKey(JNIEnv *env, jobject instance,
                                                              jstring actual_file_path_,
                                                              jstring encrypted_file_path_,
                                                              jstring key_path_) {
    const char *actual_file_path = (*env)->GetStringUTFChars(env, actual_file_path_, 0);
    const char *encrypted_file_path = (*env)->GetStringUTFChars(env, encrypted_file_path_, 0);
    const char *key_path = (*env)->GetStringUTFChars(env, key_path_, 0);

    // TODO

    (*env)->ReleaseStringUTFChars(env, actual_file_path_, actual_file_path);
    (*env)->ReleaseStringUTFChars(env, encrypted_file_path_, encrypted_file_path);
    (*env)->ReleaseStringUTFChars(env, key_path_, key_path);
}*/
