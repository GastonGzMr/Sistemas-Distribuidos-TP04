# Sistemas-Distribuidos-TP04

Este repositorio contiene las clases en java correspondientes a la parte práctica del Trabajo Práctico 04 de la materia Sistemas Distribuidos: "Comunicación en Grupos".
Cada carpeta contiene las clases necesarias para el coordinador o los procesos externo o miembro del grupo.

La clase MainProceso se utiliza para enviar un mensaje a todo el grupo a través de su nodo coordinador, o recibir mensajes del grupo aceptándolos o no.

La clase MainCoordinador permite al usuario agregar procesos al grupo, o esperar mensajes de un proceso externo para reenviarlo a todos los miembros del grupo.
En este caso, si uno de los miembros no acepta el mensaje o no responde, ordena a los demás miembros descartarlo.
