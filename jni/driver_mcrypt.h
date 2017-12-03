#ifndef WFILE_H
#define WFILE_H

int decryptFile(const char *, const char *);

int encryptFile(const char *, const char *);

int decryptFileWithKey(const char *, const char *, const char *);

int encryptFileWithKey(const char *, const char *, const char *);

char *callMyString(const char *);

#endif
