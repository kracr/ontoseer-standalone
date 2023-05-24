# ontoseer-standalone
This the standalone version of OntoSeer, i.e., it will not be a Protege plugin and can run independently.
### Using Docker Image
1. Build the image using docker build command: docker build -t <name of the docker image to be created> .
2. docker images
3. docker run -v <path to the folder that contains the owl file>:/data --rm <name of the docker image to be created> -p /data/<name of the owl file with .owl extension> -cr CusineType -pr hasCusineType -ar HalfMove -or Cooking Dish -chr y y N Y -vr Cooking
