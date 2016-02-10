function onLoad() {

    $(".row-contener.left").each(function(i, el) {
    if (localStorage['leftSelect' + i] == 'favorites') {
        $(this).addClass('row-contener-select');
	}});
	
    $(".row-contener.right").each(function(i, el) {
        if (localStorage['rightSelect' + i] == 'favorites') {
			alert('rightSelect');
            $(this).addClass('row-contener-select');
            window.localStorage.removeItem('rightSelect' + i);
        }});
		
	$(".services-left-menu li a").each(function(i, el) {
        if (localStorage['servicesLeft' + i] == 'favorites') {
            $(this).addClass('services-left-menu-select');
            window.localStorage.removeItem('servicesLeft' + i);
        }});
		
    //window.localStorage.clear();//.removeItem(key);
    $( ".row-contener.left" ).click(selectRowleft);
    $( ".row-contener.right" ).click(selectRowright);
    $( ".services-left-menu li a" ).click(selectServicesLeftMenu);
}

function selectRowleft(){
    $( ".row-contener.left" ).removeClass("row-contener-select");
    $( this ).addClass( "row-contener-select" );
    window.localStorage.clear();
    var $item = $(this).closest('.row-contener.left');
    var index = $('.row-contener.left').index($item);
    localStorage.setItem('leftSelect' + index, 'favorites');
}
function selectRowright(){
    $( ".row-contener.right" ).removeClass("row-contener-select");
    $( this ).addClass( "row-contener-select" );
    var $item = $(this).closest('.row-contener.right');
    var index = $('.row-contener.right').index($item);
    localStorage.setItem('rightSelect' + index, 'favorites');

}

function selectServicesLeftMenu(){
    /*$( ".services-left-menu li a" ).removeClass("services-left-menu-select");
    $( this ).addClass( "services-left-menu-select" );
    var $item = $(this).closest('.services-left-menu li a');
    var index = $('.services-left-menu li a').index($item);
    localStorage.setItem('servicesLeft' + index, 'favorites');*/

}

  function animateText(id, text, i) {
    document.getElementById(id).innerHTML = text.substring(0, i);
    i++;
    if (i > text.length) i = 0;
    setTimeout("animateText('" + id + "','" + text + "'," + i + ")", 100);
  }