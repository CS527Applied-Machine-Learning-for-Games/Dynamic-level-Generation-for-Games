sh ./scripts/clean.sh > cleanup.log
javac Agent/*.java
javac MarioSDK/component/*.java
javac MarioSDK/effects/*.java
javac MarioSDK/graphics/*.java
javac MarioSDK/helper/*.java
javac MarioSDK/sprites/*.java
javac *.java

mkdir ../target
mkdir ../target/MarioSDK
mkdir ../target/Agent
mkdir ../target/MarioSDK/component
mkdir ../target/MarioSDK/effects
mkdir ../target/MarioSDK/graphics
mkdir ../target/MarioSDK/helper
mkdir ../target/MarioSDK/sprites

#mv -f ./MarioSDK/component/*.class ../target/MarioSDK/component/
#mv -f ./Agent/*.class ../target/Agent
#mv -f ./MarioSDK/effects/*.class ../target/MarioSDK/effects/
#mv -f ./MarioSDK/graphics/*.class ../target/MarioSDK/graphics/
#mv -f ./MarioSDK/helper/*.class ../target/MarioSDK/helper/
#mv -f ./MarioSDK/sprites/*.class ../target/MarioSDK/sprites/
#mv -f ./*.class ../target
#cp -r -f ./img ../target

