#ifndef MOBICRYPT_H
#define MOBICRYPT_H


int mobi_crypt(char *plaintext, unsigned int len, char *ctext);
int mobi_decrpt(char *plaintext, unsigned int len, char *ctext);
int mobi_cryptWithKey(char *plaintext, unsigned int len, char *ctext, unsigned char *key);
int mobi_decrptWithKey(char *plaintext, unsigned int len, char *ctext, unsigned char *key);


#endif
