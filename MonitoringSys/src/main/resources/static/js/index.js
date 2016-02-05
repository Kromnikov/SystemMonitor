function onLoad() {
    $(".row-contener.host").each(function(i, el) {
    if (localStorage['hostSelect' + i] == 'favorites') {
        $(this).addClass('row-contener-select');
   }});
    $(".row-contener.metric").each(function(i, el) {
        if (localStorage['metricSelect' + i] == 'favorites') {
            $(this).addClass('row-contener-select');
            window.localStorage.removeItem('metricSelect' + i);
        }});
    //window.localStorage.clear();//.removeItem(key);


    $( ".row-contener.host" ).click(selectRowHost);
    $( ".row-contener.metric" ).click(selectRowMetric);
}

function selectRowHost(){
    $( ".row-contener.host" ).removeClass("row-contener-select");
    $( this ).addClass( "row-contener-select" );


    //var hostSelect = $(this).addClass( "row-contener-select" );
    //localStorage.setItem('hostSelect', hostSelect);
    //window.localStorage.setItem('hostSelect',$(this).addClass('row-contener-select'));

    window.localStorage.clear();
    var $item = $(this).closest('.row-contener.host');
    var index = $('.row-contener.host').index($item);
    localStorage.setItem('hostSelect' + index, 'favorites');





    
}
function selectRowMetric(){
    $( ".row-contener.metric" ).removeClass("row-contener-select");
    $( this ).addClass( "row-contener-select" );
    var $item = $(this).closest('.row-contener.metric');
    var index = $('.row-contener.metric').index($item);
    localStorage.setItem('metricSelect' + index, 'favorites');

}