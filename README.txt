README
-------------------------------
Ali, Prahlad, Vaishali

The contents of this folder include -
-Project Presentation (Slides)
-Project Report
-Project Paper
-4 java files and a makefile
-3 priv/pub key pairs
-3 certificates and their detail files.
-master file
-blockchain file
-readme

HOW TO RUN
-------------------------------

1. Use make command to compile all the files.
2. Use java certificate to generate signatures
	I have put up keypairs for three users ali, prahlad and vaishali
	SHA256withRSA is the Signature Algorithm
	SHA1 is the public key algo
	Enter the otehr details as asked.
3. Use java PutCertificate to add a certificate from the master file to the blockchain.
	Each read of the master file create a new block.
4. Use java CheckBlockchain to enter the name of person whose certificate you want to check.