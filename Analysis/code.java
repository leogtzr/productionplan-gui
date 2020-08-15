double woHours = wo.getHours();			// 15.9
final double total = wo.getHours();

while (woHours > 0.0) {
	Turn turn = calcTurn(total, woHours);
	if (woHours > turn.Hours()) {
		// ...
	} else {
		// ...
	}
}