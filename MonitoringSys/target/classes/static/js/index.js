function onLoad() {

    $('li a').click(function()
    {
        //alert("window.localStorage.clear()");
       window.localStorage.clear();
    });
    $('td button').click(function()
    {
        //alert("window.localStorage.clear()");
        window.localStorage.clear();
    });

    $(".row-contener.left").each(function(i, el) {
    if (localStorage['leftSelect' + i] == 'favorites') {
        $(this).addClass('row-contener-select');
   }});
    $(".row-contener.right").each(function(i, el) {
        if (localStorage['rightSelect' + i] == 'favorites') {
            $(this).addClass('row-contener-select');
            window.localStorage.removeItem('rightSelect' + i);
        }});
    //window.localStorage.clear();//.removeItem(key);
    $( ".row-contener.left" ).click(selectRowleft);
    $( ".row-contener.right" ).click(selectRowright);
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
function resizeTo(){
    $(".center-contener-60").show().animate({opacity:0}, 0);
    $(".center-contener-60").show().animate({width:'40%'}, 0).fadeOut(1);
    $(".center-contener-60").show().animate({opacity:1,  marginLeft: '+=20%'}, 0).fadeIn(500);
}













function metricsMenu() {
    //$(".row-contener.right").each(function(i, el) {
    //    if (localStorage['rightSelect' + i] == 'favorites') {
    //        $(this).addClass('row-contener-select');
    //        window.localStorage.removeItem('rightSelect' + i);
    //    }});
    $(".services-left-menu  li a").click(function() {
        //selectMetricsMenuItem();
    });
}
function selectMetricsMenuItem(){
    alert("alert");
    $( ".services-left-menu li" ).removeClass("services-left-menu-select");
    $( this ).addClass( "services-left-menu-select" );
    window.localStorage.clear();
    var $item = $(this).closest('.services-left-menu li');
    var index = $('.services-left-menu li').index($item);
    localStorage.setItem('servicesSelect' + index, 'favorites');
}