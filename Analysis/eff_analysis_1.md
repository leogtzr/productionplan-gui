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

## 1) Iteration
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
	// Este 6.5 es especialmente importante porque nos servirá para crear la nueva orden con dicho valor y calcular las piezas por hacer ...
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
	// Este 8.1 es especialmente importante porque nos servirá para crear la nueva orden con dicho valor y calcular las piezas por hacer ...
	remainderHours = 1.3
}
```

De nuevo hemos abarcado todo el turno..., podemos pasar al siguiente.

```java
turn = nextTurn(currentTurn, numberOfTurns)
```

```java
turn = TT				// 5.5
```

```java
if (remainderHours > currentTurn.get()) {
```

En el caso anterior ahora la evaluación nos da como resultado **"false"**.
¿Qué agregaremos ahora de *else* clause?

```java
} else {
	// ...
	newOrder = createOrder(remainderHours, currentTurn)
	remainderHours = 0.0D
}
```

En el code snippet anterior **remainderHours** fue menos a el tiempo del turno
actual (TT : 5.5), entonces usamos dicho **remainderHours**

Poner la variable en 0.0 debería hacernos salir del **loop**, algo así:

```java
while (remainderHours > 0.0D) {
```

Sigue la cuestión de los días y de cómo se debe continuar, es decir, hay varios casos, o mejor dicho cómo se debe continuar el análisis.

```
[Orden Inicial][Orden que necesita análisis de eficiencia][Otra Orden ... ]
```

Lo anterior parece obligarnos a pensar en introducir parámetros al método para llevar
*track* de la suma de horas.

## Algoritmo con *track* de horas globales

```java
public static List<EfficiencyWorkOrderInformation> splitOrders(
	final WorkOrderInformation wo,
	final double initialHours,
	final double maxTurnHours,
	final int numberOfTurns
)
```

### Primera Orden

```
Hrs = 15.9
Setup = 2.0
Piezas por hacer 220 ...
Turnos = 3
```

### Segunda Orden
```
Hrs = 6.7
Setup = 1.3
Piezas por hacer 88 ...
Turnos = 3
```

### Tercer Orden

```
Hrs = 4.5
Setup = 0.7
Piezas por hacer 45 ...
Turnos = 3
```

## Corrida con Orden #1

```java
final int numberOfTurns = 3;
final int maxTurnHours = 8.5 + 8.1 + 5.5; 		// 22.1
// 22.1
double initialHours = 0.0D;
// Empezando desde 0.
```

```java
final List<EfficiencyWorkOrderInformation> orders = splitOrders(
	workOrder
	, initialHours
	, maxTurnHours
	, 3
);
```

```
Hrs = 15.9
Setup = 2.0
Piezas por hacer 220 ...
Turnos = 3
```

```java
orders = splitOrders(
	workOrder :: Order(15.9, 2.0, 220, 3 :: turns)
	, initialHours :: 0
	, maxTurnHours :: 22.1
	, 3
);
```

Primero me pregunto, ¿para qué es el initialHours o para qué lo pensaste? Quizás con algunas iteraciones se haga un poco más evidente, por lo pronto no tiene forma aún en mi cabeza.
Update 1: Se me ha ocurrido que *initialHours* servirá por lo pronto para calcular en qué turno estoy, aunque bien podría recibirse como argumento.

```
final int numberOfTurns = 3;
final totalHours = 15.9
remainderHours = totalHours
remainderHours = totalHours
currentTurn = FT 			// Initial turn.
setupHours = wo.setup()
saveSetupHours = true
```

### it-1 Ord1

```java
turnFromInitial = calculateTurnFromHours(initialHours, numberOfTurns)
~turnFromInitial = FT
```
```java
while (remainderHours > 0.0D) {
```
¿Qué papel juega initialHours en esta parte específica del algoritmo?
Ya volveremos a ello ...
```java
if (remainderHours > currentTurn.get()) {
~ if (15.9 > 8.5) {
~ true
```
En este caso remainderHours = 15.9 por lo cuál es true la evaluación.
```java
// La primera iteración "saveSetupHours" será true ... 
	if (saveSetupHours) {
		remainderHours = remainderHours - (currentTurn.get() - setupHours)
		~remainderHours = 15.9 - (8.5 - 2.0)
		~remainderHours = 15.9 - (8.5 - 2.0)
		~remainderHours = 9.4
		saveSetupHours = false
		orders.add(new Order(current.get(), 2.0))
	} else {
		// remainderHours = remainderHours - currentTurn.get()
		// pending ... 
	}
```

### it-2 Ord1

```java
currentTurn = nextTurn(currentTurn, numberOfTurns)
~currentTurn = ST
~currentTurn = 8.1
```

```java
while (remainderHours > 0.0D) { 			// <- true
~while (9.4 > 0.0D) {
~while (true) {
```

```java
	if (saveSetupHours) {
	~if (false) {
```

```java
	} else {
		remainderHours = remainderHours - currentTurn.get()
		~remainderHours = 9.4 - currentTurn.get()
		~remainderHours = 9.4 - 8.1
		~remainderHours = 1.3
	}
```