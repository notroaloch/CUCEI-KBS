# I5913 - Sistemas Basados en Conocimiento

Ochoa Herrera Rodrigo Alejandro  
CUCEI | Universidad de Guadalajara  
I5913 - Sistemas Basados en Conocimiento

### Compilar y Ejecutar

Challenge 1
```
javac -cp lib\CLIPSJNI.jar src\Clips.java -d classes\

java -cp lib\CLIPSJNI.jar;classes\ clips.Clips
```

Challenge 2
```
javac -cp lib\jade.jar;lib\CLIPSJNI.jar src\ClipsAgent.java -d classes\

java -cp lib\jade.jar;lib\CLIPSJNI.jar;classes\ jade.Boot -gui clipsAgent:clips.ClipsAgent
```