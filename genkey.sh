#!/bin/bash
keytool -genkeypair -alias JettySSL -dname "CN=Bobby White, OU=Chatter Messaging, O=Salesforce.com, L=Charlotte, ST=NC, C=US" -keystore ./keystore
