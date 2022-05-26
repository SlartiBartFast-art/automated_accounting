# How does one use it?

- Get Postman
- Run it. Don't bother signing in if you don't want to, there's a small link on the bottom to skip. This project does
  not use any of Postman's cloud features.
- Click Import button in top left and drag all the files from the project there
- In top right corner select your environment (sandbox or production).
- If you selected sandbox, run Create a sandbox user request. It will automatically save your API key to the
  environment.
- If you selected production, edit the environment using the "eye" icon. Set "current value" of api_key to your API key.
- Run Create an installation, Add the device, Add a session in that order. They will create everything needed for using
  public API, including RSA keys, session and installation tokens. For you reference all those values will be set in the
  environment.
- Run GET - List monetary accounts to check that everything is working properly. If it does, you can modify that request
  to do whatever you wanted to do and "Save as" for your future use.