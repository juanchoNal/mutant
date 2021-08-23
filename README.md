Este es el resultado de la prueba para saber si de acuerdo de una matriz que contiene el ADN de una persona es mutante o humano.
ANOTACIONES GENERALES:
Como el ejercicio decía que la matriz eran N*N, asumí que solo aplican para matrices cuadradas
El algoritmo lo organice en 4 pasos, recorrer las filas inicialmente; después paso las columnas a filas y las reccoro; después recorro las diagonales de izquierda a derecha y por último hago un espejo de la matriz para volver a recorrer la matriz de la diagonales inversas.
Para no recorrer toda la matriz sin necesidad estoy mirando que las posiciones que faltan por comparar no sean menor a 4, o si ya encontré 2 secuencias de la que me dice que es mutante en ese instante para y guardo el resyltado como mutante.


Para la ejecución: 

curl --request POST \
  --url http://prueba-env.eba-xhhrm9iy.sa-east-1.elasticbeanstalk.com/mutant/ \
  --header 'Content-Type: application/json' \
  --data '{
	"dna": [
		"ATGCGA",
		"CAGTGC",
		"TTATGT",
		"AGAAGG",
		"CCCCTA",
		"TCACTG"
	]
}'
si es  mutante devolvera un HTTP 200-OK; de no ser mutante devolvera 403-Forbidden

Para saber las estadisticas;

curl --request GET \
  --url http://prueba-env.eba-xhhrm9iy.sa-east-1.elasticbeanstalk.com/stats
  
  



