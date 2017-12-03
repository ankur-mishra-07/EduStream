#include <stdio.h>
#include <memory.h>
#include "aes_lcl.h"


int mobi_cryptWithKey(char *plaintext, unsigned int len, char *ctext, unsigned char *key_in) {

    unsigned int key_schedule[60];
    unsigned char iv[16] = {0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b,
                            0x0c, 0x0d, 0x0e, 0x0f};

    unsigned char lclCtext[128];
    int i = 0;
    memset(lclCtext, 0x00, 128);

    memset(key_schedule, 0x00, 60);

    aes_key_setup(key_in, &key_schedule[0], 256);
    aes_encrypt_cbc(plaintext, 64, ctext, key_schedule, 256, iv);
   /* for (i = 0; lclCtext[i] != 0x00; i++) {
        ctext[i] = lclCtext[i];
    }
    ctext[i] = '\0';*/
    return 0;

}


int mobi_decrptWithKey(char *plaintext, unsigned int len, char *rtext, unsigned char *key_in) {

    unsigned int key_schedule[60];
    unsigned char iv[16] = {0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b,
                            0x0c, 0x0d, 0x0e, 0x0f};

    unsigned char lclCtext[128];
    int i = 0;
    memset(lclCtext, 0x00, 128);

    memset(key_schedule, 0x00, 60);
    aes_key_setup(key_in, key_schedule, 256);
    aes_decrypt_cbc(plaintext, 64, rtext, key_schedule, 256, iv);


    return 0;
}






