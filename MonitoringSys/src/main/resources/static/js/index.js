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