#include <stdio.h>
#include <memory.h>
#include "aes_lcl.h"
#include <stdlib.h>
#include "mobicrypt.h"
#include <android/log.h>

#define APPNAME "MyApp"

void callMe() {
//úýhÝ3ÍéÍ±-*Ü3
    unsigned char ptext[32] = "úýhÝ3ÍéÍ±-*Ü3";
    unsigned char ctext[128];
    unsigned char rtext[128];
    memset(ctext, 0x00, 128);
    memset(rtext, 0x00, 128);
    __android_log_print(ANDROID_LOG_VERBOSE, APPNAME, "%d \n", strlen(ptext));
    printf("%d \n", strlen(ptext));
    //mobi_crypt(ptext, strlen(ptext), ctext);

    __android_log_print(ANDROID_LOG_VERBOSE, APPNAME, "ctext \n");
    printf("ctext \n");
    for (int i = 0; i < 128; i++) {
        printf("%02x", ctext[i]);
        __android_log_print(ANDROID_LOG_VERBOSE, APPNAME, "%02x", ctext[i]);
    }
    //printf("text: %s\n",ctext);
   // mobi_decrpt(ctext, 128, rtext);
    printf("text: %s\n", rtext);
    __android_log_print(ANDROID_LOG_VERBOSE, APPNAME, "%s\n", rtext);
}
