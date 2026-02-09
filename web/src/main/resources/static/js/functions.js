function formatFloatText() {
	$('.float-text').each(function() {
		$(this).text(parseFloat($(this).text()).toFixed(2));
	});
}