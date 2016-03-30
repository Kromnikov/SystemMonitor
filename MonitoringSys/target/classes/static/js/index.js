function dellAlarm() {

}

function checking() {
        console.log('checking');
        $.getJSON('/getAlarms?userName=anton', function (data) {
            $("#dropdown-menu").empty();
            $("#dropdown-menu-length").empty();
            $("#dropdown-menu-length").append(Object.keys(data).length);
            $.each(data, function (key,values) {
                //console.log(values.id);
                if(values.type=="error"){
                    $("#dropdown-menu").append('<li><a href="#"><b class="label label-warning label-min fa fa-frown-o white-text fa-lg">&nbsp;</b>'+values.message+'' +
                    '<b idalarm="'+values.id+'"  class="dropdown-menu-right fa fa-times fa-lg"></b></a></li>');
                }else{
                    $("#dropdown-menu").append('<li><a href="#"><b class="label label-success label-min fa fa-smile-o white-text fa-lg">&nbsp;</b>'+values.message+'' +
                    '<b  idalarm="'+values.id+'"  class="dropdown-menu-right fa fa-times fa-lg"></b></a></li>');
                }
            });
        });
}
function start_checking() {
    setTimeout(function(){
        console.log('checking');
        $.getJSON('/getAlarms?userName=anton', function (data) {
            $("#dropdown-menu").empty();
            $("#dropdown-menu-length").empty();
            $("#dropdown-menu-length").append(Object.keys(data).length);
            $.each(data, function (key,values) {
                if(values.type=="error"){
                    $("#dropdown-menu").append('<li><a href="#"><b class="label label-warning label-min fa fa-frown-o white-text fa-lg">&nbsp;</b>'+values.message+'' +
                    '<b idalarm="'+values.id+'"  class="dropdown-menu-right fa fa-times fa-lg"></b></a></li>');
                }else{
                    $("#dropdown-menu").append('<li><a href="#"><b class="label label-success label-min fa fa-smile-o white-text fa-lg">&nbsp;</b>'+values.message+'' +
                    '<b idalarm="'+values.id+'"  class="dropdown-menu-right fa fa-times fa-lg"></b></a></li>');
                }
            });
        });
        start_checking();
    }, 10000);
}

function showProblemHost() {
    $(document).ready(function () {
        //$("#hostProblems").removeClass("hidden");
        //$("#hostListTable").toggleClass('hidden');
        $("#hostListTable").removeClass('hidden');
    });
    //console.log('lal1');
}
function showChart() {
    $(document).ready(function () {
        $("#chart_1").removeClass("hidden");
        $("#hostListTable").addClass('hidden');
    });
}
function showMetrics() {
    $(document).ready(function () {
        //$("#hostMetrics").removeClass("hidden");
        //if(!$("#hostListTable").hasClass('hidden'))
        //$("#hostListTable").addClass('hidden');
        $("#hostListTable").removeClass('hidden');
    });
}
function hideShowHostListContent() {
    checking();
    start_checking();

    $('b').click(function () {
        console.log($(this).attr('idalarm'));
        $.getJSON('/dellAlarm?id='+$(this).attr('idalarm'), function (data) {

        });
        checking();
    });
    $('.fa-expand').click(function () {
        $(this).toggleClass('fa-expand');
        $(this).toggleClass('fa-compress');
        //$(this).parent().toggleClass('expand');
        $(this).parent().toggleClass('margin-top-20');
        $(this).parent().children('.content-panel').toggleClass('expand');
    });
    $('h4').click(function () {
        //console.log('dell where id= '+$(this).attr('idalarm'));
        $(this).children('i').toggleClass('fa-angle-right');
        $(this).children('i').toggleClass('fa-angle-down');
        $(this).parent().children('table').toggleClass('hidden');
    });
}


function onLoad() {

    $('li a').click(function () {
        //alert("window.localStorage.clear()");
        window.localStorage.clear();
    });
    $('td button').click(function () {
        //alert("window.localStorage.clear()");
        window.localStorage.clear();
    });

    $(".row-contener.left").each(function (i, el) {
        if (localStorage['leftSelect' + i] == 'favorites') {
            $(this).addClass('row-contener-select');
        }
    });
    $(".row-contener.right").each(function (i, el) {
        if (localStorage['rightSelect' + i] == 'favorites') {
            $(this).addClass('row-contener-select');
            window.localStorage.removeItem('rightSelect' + i);
        }
    });
    //window.localStorage.clear();//.removeItem(key);
    $(".row-contener.left").click(selectRowleft);
    $(".row-contener.right").click(selectRowright);
}
function selectRowleft() {
    $(".row-contener.left").removeClass("row-contener-select");
    $(this).addClass("row-contener-select");
    window.localStorage.clear();
    var $item = $(this).closest('.row-contener.left');
    var index = $('.row-contener.left').index($item);
    localStorage.setItem('leftSelect' + index, 'favorites');
}
function selectRowright() {
    $(".row-contener.right").removeClass("row-contener-select");
    $(this).addClass("row-contener-select");
    var $item = $(this).closest('.row-contener.right');
    var index = $('.row-contener.right').index($item);
    localStorage.setItem('rightSelect' + index, 'favorites');

}
function resizeTo() {
    $(".center-contener-60").show().animate({opacity: 0}, 0);
    $(".center-contener-60").show().animate({width: '40%'}, 0).fadeOut(1);
    $(".center-contener-60").show().animate({opacity: 1, marginLeft: '+=20%'}, 0).fadeIn(500);
}
function metricsMenu() {
    //$(".row-contener.right").each(function(i, el) {
    //    if (localStorage['rightSelect' + i] == 'favorites') {
    //        $(this).addClass('row-contener-select');
    //        window.localStorage.removeItem('rightSelect' + i);
    //    }});
    $(".services-left-menu  li a").click(function () {
        //selectMetricsMenuItem();
    });
}
function selectMetricsMenuItem() {
    alert("alert");
    $(".services-left-menu li").removeClass("services-left-menu-select");
    $(this).addClass("services-left-menu-select");
    window.localStorage.clear();
    var $item = $(this).closest('.services-left-menu li');
    var index = $('.services-left-menu li').index($item);
    localStorage.setItem('servicesSelect' + index, 'favorites');
}

