#include <stdio.h>
#include <memory.h>
#include "mobicrypt.h"
#include <android/log.h>

#define APPNAME "MyApp"

int encryptFileWithKey(const char *actual_file_path, const char *encrypted_file_path,
                       const char *key_in) {

    char actual_file[128];
    char encrypted_file[128];

    sprintf(actual_file, "%s", actual_file_path);
    sprintf(encrypted_file, "%s", encrypted_file_path);

    __android_log_print(ANDROID_LOG_VERBOSE, APPNAME,
                        "Actual file      %s", actual_file);
    __android_log_print(ANDROID_LOG_VERBOSE, APPNAME,
                        "Encrypted file         %s", encrypted_file);


    unsigned char key[32];
    sprintf(key, "%s", key_in);

    int ch;
    int count = 0;
    FILE *fptr_input;
    unsigned char inputArray[128];
    unsigned char ctext[128];
    memset(ctext, 0x00, 128);


    //Reading from actual file and storing it in an array
    fptr_input = fopen(actual_file, "r");
    if (fptr_input == NULL) {
        perror("Error");
        __android_log_print(ANDROID_LOG_VERBOSE, APPNAME,
                            "1.Error opening  for reading. Program terminated....%s", actual_file);
        return 0;
    }
    while ((ch = fgetc(fptr_input)) != EOF) {
        inputArray[count] = (unsigned char) ch;
        count++;
    }

    inputArray[count] = '\0';
    fclose(fptr_input);

    __android_log_print(ANDROID_LOG_VERBOSE, APPNAME, "Input text is %s", inputArray);

    //NOW passing that values to encrypt
    mobi_cryptWithKey(inputArray, 11, ctext, key_in);
    __android_log_print(ANDROID_LOG_VERBOSE, APPNAME, "Encrypted text is %s", ctext);


    //SAVES THE RESULT IN destination file
    FILE *fptr_enc = NULL;
    fptr_enc = fopen(encrypted_file, "w");
    if (fptr_enc == NULL) {
        perror("Error");
        __android_log_print(ANDROID_LOG_VERBOSE, APPNAME,
                            "2.Error opening  for writing. Program terminated..... %s",
                            encrypted_file);
        return 0;
    }

    for (int i = 0; i < 128; i++) {
        fputc(ctext[i], fptr_enc);
    }
    fclose(fptr_enc);
    return 1;
}


//NOW FETCH VALUE FROM ENCRYPT TX FILE AND PASS IT TO DECRYPT
int decryptFileWithKey(const char *encrypted_file_path, const char *decrypted_file_path,
                       const char *key_in) {

    char decypted_file[128];
    sprintf(decypted_file, "%s", decrypted_file_path);

    char encrypted_file[128];
    sprintf(encrypted_file, "%s", encrypted_file_path);

    __android_log_print(ANDROID_LOG_VERBOSE, APPNAME,
                        "Actual file      %s", encrypted_file);
    __android_log_print(ANDROID_LOG_VERBOSE, APPNAME,
                        "Encrypted file         %s", decypted_file);


    unsigned char outputArray[128];
    unsigned char rtext[128];
    memset(rtext, 0x00, 128);

    unsigned char key[32];
    sprintf(key, "%s", key_in);
    int ch_de;
    int count_de = 0;
    FILE *fptr_encrypted;


// Fetching contents from encrypted file to array

    fptr_encrypted = fopen(encrypted_file, "r");
    if (fptr_encrypted == NULL) {
        perror("Error");
        __android_log_print(ANDROID_LOG_VERBOSE, APPNAME,
                            "3.Error opening  for reading. Program terminated..... %s",
                            encrypted_file);
        return 0;
    }
    while ((ch_de = fgetc(fptr_encrypted)) != EOF) {
        outputArray[count_de] = (unsigned char) ch_de;
        count_de++;
    }
    outputArray[count_de] = '\0';
    fclose(fptr_encrypted);
    __android_log_print(ANDROID_LOG_VERBOSE, APPNAME, "Input text is %s", outputArray);


    //PASS array to decrypt
    mobi_decrptWithKey(outputArray, 11, rtext, key_in);

    //NOW SAVE THE  DECRYPTED TX FILE  destination file
    FILE *fptr_dec;
    fptr_dec = fopen(decypted_file, "w");
    if (fptr_dec == NULL) {
        perror("Error");
        __android_log_print(ANDROID_LOG_VERBOSE, APPNAME,
                            "4.Error opening  for writing. Program terminated.....%s",
                            decypted_file);
        return 0;

    }
    for (int i = 0; i < 16; i++) {
        fputc(rtext[i], fptr_dec);
    }
    fclose(fptr_dec);

    __android_log_print(ANDROID_LOG_VERBOSE, APPNAME, "Decrypted text is %s", rtext);

    return 1;
}






