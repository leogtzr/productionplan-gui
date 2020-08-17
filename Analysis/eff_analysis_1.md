Hrs = 15.9
Setup = 2.0
Piezas por hacer 220 ...
Turnos = 3

FT = 8.5            (First Turn)
ST = 8.1            (Second Turn)
TT = 5.5            (Third Turn)




FT ... entonces, solo podemos abarcar 8.5 de la orden
wo1 = Orden(6.5 :: hours, 2.0 :: setup)
Para la creación de la orden anterior se tomaron en cuenta diversas cosas: 
- El turno en el que estoy parado, es decir FT (8.5)
- La operación FT (8.5) - setup (2.0) que da como resultado: 6.5 para hours y 2.0 para setupHours
- Por alguna razón pienso que debe haber una variable boolean que ayudaría a verificar si debe de usarse run hours o no para 
	otra iteración.

it-1)
wo1 = Orden(6.5 :: hours, 2.0 :: setup)
Según los comentarios de Miriam, tenemos ya en esta primera orden 6.5 cubiertos de 15.9 (total)
total - 6.5 = 9.4

ya hemos cubierto el primer turno ..., o mejor dicho tenemos que pasar al siguiente turno.
turn = nextTurn(currentTurn, numberOfTurns)
En este caso nos dará ST, 8.1

remanente = total - 6.5
Que quizás también puede ser expresado así:
runHoursDeLaOrdenAnterior = previousOrderHours(idx)
remanente = total - runHoursDeLaOrdenAnterior

remanente = 15.9 - 6.5
remanente = 9.4

it-2)
9.4 y estamos en ST (8.1), en este caso podemos visualizar que para el segundo turno solo podemos abarcar 8.1
wo2 = Orden(8.1 :: hours, setup :: 0.0)

Cálculos:
remanente = 9.4 - 8.1
remanente = 1.3

it-3)
turn = nextTurn(currentTurn, numberOfTurns)
turn = TT
turn = 5.5 available ...

en este caso podemos abarcar parte de este turno y no todo.

remanente = 15.9 - 8.1 - 6.5 ...

Va tomando forma ... 




Esta vez intentando ponerle más control y usar código que se acerque más a la realidad.

Hrs = 15.9
Setup = 2.0
Piezas por hacer 220 ...
Turnos = 3

FT = 8.5            (First Turn)
ST = 8.1            (Second Turn)
TT = 5.5            (Third Turn)

```
final totalHours = 15.9
remainderHours = totalHours			 
remainderHours = totalHours
currentTurn = FT 			// Initial turn.
setupHours = wo.setup()
saveSetupHours = true
```

Potential method signature ... 
```java
List<WorkOrderInformation> splitOrders(final WorkOrderInformation wo)
```

# 1) Iteration
```java
while (remainderHours > 0.0D) 
```

### Idea
Podemos comparar si **remainderHours** cabe en el turno actual.

```java
if (remainderHours > currentTurn.get()) {

}
```

Si lo anterior es **true** entonces podemos abarcar todo ese turno

```java
if (remainderHours > currentTurn.get()) {
	remainderHours -= (currentTurn.get() - setupHours)
} else {
	// ...ya veremos ...
}
```


sustiyendo las variables ...
```java
if (remainderHours > currentTurn.get()) {
	// La primera iteración "saveSetupHours" será true ... 
	if (saveSetupHours) {
		remainderHours = remainderHours - (currentTurn.get() - setupHours)
		saveSetupHours = false
	} else {
		remainderHours = remainderHours - currentTurn.get()
	}

	remainderHours = 15.9 - (8.5 - 2.0)
	remainderHours = 15.9 - 6.5
	remainderHours = 9.4
}
```

como abarcamos todo el turno podemos pasar al siguiente ... 
```java
turn = nextTurn(currentTurn, numberOfTurns)
```

```java
turn = ST				// 8.1
```

Podemos volver a preguntar 
```java
if (remainderHours > currentTurn.get()) {
```

```java
if (remainderHours > currentTurn.get()) {
	// if (saveSetupHours) { 				<----- false
	remainderHours = remainderHours - currentTurn.get()
	remainderHours = 9.4 - 8.1
	remainderHours = 1.3
}
```

De nuevo hemos abarcado todo el turno ... 
