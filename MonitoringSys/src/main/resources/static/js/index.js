function onLoad() {
    $( ".row-contener.host" ).click(selectRowHost);
    $( ".row-contener.metric" ).click(selectRowMetric);
}

function selectRowHost(){
    $( ".row-contener.host" ).removeClass("row-contener-select");
    $( this ).addClass( "row-contener-select" );
}
function selectRowMetric(){
    $( ".row-contener.metric" ).removeClass("row-contener-select");
    $( this ).addClass( "row-contener-select" );
}