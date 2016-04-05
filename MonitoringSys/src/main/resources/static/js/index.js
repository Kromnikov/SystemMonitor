var username;


function dropDownMenuAccounts() {
    $(document).ready(function(){
        //$('a').on('click', function(e){
        //    e.preventDefault();
        //});

        $('#ddmenu').hover(function () {
            clearTimeout($.data(this,'timer'));
            $('ul',this).stop(true,true).slideDown(200);
        }, function () {
            $.data(this,'timer', setTimeout($.proxy(function() {
                $('ul',this).stop(true,true).slideUp(200);
            }, this), 100));
        });

    });
}

//TODO: Модальные окна редактирования accounts
function modalAccounts() {

    $(document).ready(function () {
        //$('.popup.accounts, .overlay.accounts').css({'opacity': 1, 'visibility': 'visible'});
        var a;
        $('body').on('click', '.roleselect', function () {
            var role=$(this).attr('role');
            //$("#mainDdmenu").val(role);
            $("#roleValue").html(role);
        });

        $('.open_window').click(function (e) {
            $.getJSON('/getAccounts?username=' + $(this).parent().parent().parent().attr('id'), function (user) {
                console.log(user.username);
                $("input[name='id']").val(user.id);
                $("input[name='Username']").val(user.username);
                $("input[name='Password']").val(user.password);
                //$("input[name='Role']").val(user.role);

                $("#ddmenu").empty();
                $("#ddmenu").append('<li id="mainDdmenu"><a id="roleValue" href="#">'+user.role+'</a></li>');

                a = $("#mainDdmenu").append('<ul id="noneMenu"></ul>');
                $.each(user.allRoles, function (key, values) {
                    $("#noneMenu").append('<li class="roleselect" role="'+values+'"><a href="#">'+values+'</a></li>');
                });

                $('.popup.accounts, .overlay.accounts').css({'opacity': 1, 'visibility': 'visible'});
                e.preventDefault();
            });
        });
        $("#saveAccount").click(function () {
            $.getJSON('/saveAccount?id=' + $("input[name='id']").val()
                +'&username='+$("input[name='Username']").val()
                +'&password='+$("input[name='Password']").val()
                +'&role='+$("#roleValue").html()
                , function (host) {
                });
            $('.popup, .overlay').css({'opacity': 0, 'visibility': 'hidden'});
            window.location.href = "/accounts";
        });


        $('.close_window').click(function () {
            $('.popup, .overlay').css({'opacity': 0, 'visibility': 'hidden'});
            window.location.href = "/accounts";
        });

    });
}




















//TODO: Модальные окна редактирования templ
function modalTemplMetirc() {

    $(document).ready(function () {

        //dell
        $('.dellTemplMetric').click(function () {
            $.getJSON('/dellTemplMetric?metricId=' + $(this).parent().parent().attr('id'), function (templMetric) {

            });
        });



        //Add
        $('.openAddTemplate').click(function (e) {
                $('.popup.addtemplMetric, .overlay.addtemplMetric').css({'opacity': 1, 'visibility': 'visible'});
                e.preventDefault();
        });
        $("#addTemplMetric").click(function () {
            $.getJSON('/addTemplMetric?id=' + $("input[name='sid']").val()
                +'&title='+$("input[name='sTitle']").val()
                +'&command='+$("input[name='sCommand']").val()
                +'&minValue='+$("input[name='sMin Value']").val()
                +'&maxValue='+$("input[name='sMax Value']").val()
                , function (host) {
                });
            $('.popup, .overlay').css({'opacity': 0, 'visibility': 'hidden'});
            window.location.href = "/templMetrics";
        });


        //Edit
        $('.open_window').click(function (e) {
            $.getJSON('/getTemplMetric?metricId=' + $(this).parent().parent().parent().attr('id'), function (templMetric) {
                //console.log(host.name+"-"+host.host);
                $("input[name='id']").val(templMetric.id);
                $("input[name='Title']").val(templMetric.title);
                $("input[name='Command']").val(templMetric.command);
                $("input[name='Min Value']").val(templMetric.minValue);
                $("input[name='Max Value']").val(templMetric.maxValue);

                $('.popup.templMetric, .overlay.templMetric').css({'opacity': 1, 'visibility': 'visible'});
                e.preventDefault();
            });
        });
        $("#saveTemplMetric").click(function () {
            $.getJSON('/saveTemplMetric?id=' + $("input[name='id']").val()
                +'&title='+$("input[name='Title']").val()
                +'&command='+$("input[name='Command']").val()
                +'&minValue='+$("input[name='Min Value']").val()
                +'&maxValue='+$("input[name='Max Value']").val()
                , function (host) {
                });
            $('.popup, .overlay').css({'opacity': 0, 'visibility': 'hidden'});
            window.location.href = "/templMetrics";
        });


        $('.close_window').click(function () {
            $('.popup, .overlay').css({'opacity': 0, 'visibility': 'hidden'});
            window.location.href = "/templMetrics";
        });

    });
}






















//TODO: Модальные окна редактирования хостов

function modalEditHostMetrics() {
    $(document).ready(function () {
        $('.open_inst_metrics').click(function (e) {
            $("#InstanceMetric").empty();
            $("#TemplateMetric").empty();
            $.getJSON('/getInstMetrics?hostid=' + $(this).parent().parent().parent().attr('id'), function (metrics) {
                $.each(metrics.instanceMetrics, function (key, values) {
                    $("#InstanceMetric").append('<tr><td>'+values.title+'</td><td><instance hostId="'+metrics.hostId+'" instMetricId="'+values.id+'" class="fa fa-times fa-lg hovercolorredtext hovercursor"></instance></td></tr>');
                });
                $.each(metrics.templateMetrics, function (key, values) {
                    $("#TemplateMetric").append('<tr><td>'+values.title+'</td><td><template hostId="'+metrics.hostId+'" templMetricId="'+values.id+'" class="fa fa-check fa-lg hovercolorgreentext hovercursor"></template></td></tr>');
                });
            });
            $('.popup.services, .overlay.services').css({'opacity': 1, 'visibility': 'visible'});
            $('.popup.services').css({'width':'700px'});



            $('body').on('click', 'template', function () {
                console.log($(this).attr('templMetricId'));
                $("#InstanceMetric").empty();
                $("#TemplateMetric").empty();
                $.getJSON('/addInstMetric?hostid=' + $(this).attr('hostId')+'&templMetricid='+$(this).attr('templMetricid'), function (metrics) {
                    $.each(metrics.instanceMetrics, function (key, values) {
                        $("#InstanceMetric").append('<tr><td>'+values.title+'</td><td><instance hostId="'+metrics.hostId+'" instMetricId="'+values.id+'" class="fa fa-times fa-lg hovercolorredtext hovercursor"></instance></td></tr>');
                    });
                    $.each(metrics.templateMetrics, function (key, values) {
                        $("#TemplateMetric").append('<tr><td>'+values.title+'</td><td><template hostId="'+metrics.hostId+'" templMetricId="'+values.id+'" class="fa fa-check fa-lg hovercolorgreentext hovercursor"></template></td></tr>');
                    });
                });
            });



            $('body').on('click', 'instance', function () {
                console.log($(this).attr('instMetricId'));
                $("#InstanceMetric").empty();
                $("#TemplateMetric").empty();
                $.getJSON('/dellInstMetric?hostid=' + $(this).attr('hostId')+'&instMetricid='+$(this).attr('instMetricId'), function (metrics) {
                    $.each(metrics.instanceMetrics, function (key, values) {
                        $("#InstanceMetric").append('<tr><td>'+values.title+'</td><td><instance hostId="'+metrics.hostId+'" instMetricId="'+values.id+'" class="fa fa-times fa-lg hovercolorredtext hovercursor"></instance></td></tr>');
                    });
                    $.each(metrics.templateMetrics, function (key, values) {
                        $("#TemplateMetric").append('<tr><td>'+values.title+'</td><td><template hostId="'+metrics.hostId+'" templMetricId="'+values.id+'" class="fa fa-check fa-lg hovercolorgreentext hovercursor"></template></td></tr>');
                    });
                });
            });
        });

        //$('.popup.services, .overlay.services').css({'opacity': 1, 'visibility': 'visible'});
        //$('.popup.services').css({'width':'700px'});
        //$("#InstanceMetric").append('<tr><td>getCPU</td><td><i class="fa fa-times fa-lg hovercolorredtext hovercursor"></i></td></tr>');
    });

}
function modalEditHost() {

    $(document).ready(function () {
            $('.open_window').click(function (e) {
                $.getJSON('/gethost?hostid=' + $(this).parent().parent().parent().attr('id'), function (host) {
                    //console.log(host.name+"-"+host.host);
                    $("input[name='id']").val(host.id);
                    $("input[name='name']").val(host.name);
                    $("input[name='host']").val(host.host);
                    $("input[name='port']").val(host.port);
                    $("input[name='login']").val(host.login);
                    $("input[name='password']").val(host.password);
                    $("input[name='location']").val(host.location);

                    $('.popup.host, .overlay.host').css({'opacity': 1, 'visibility': 'visible'});
                    e.preventDefault();
                });
            });
        $("#saveHost").click(function () {
            $.getJSON('/saveHost?id=' + $("input[name='id']").val()
                +'&name='+$("input[name='name']").val()
                +'&host='+$("input[name='host']").val()
                +'&port='+$("input[name='port']").val()
                +'&login='+$("input[name='login']").val()
                +'&password='+$("input[name='password']").val()
                +'&location='+$("input[name='location']").val()
                , function (host) {
            });
            $('.popup, .overlay').css({'opacity': 0, 'visibility': 'hidden'});
            window.location.href = "/hostedit";
        });

        //$('.popup .close_window, .overlay').click(function (){
        $('.close_window').click(function () {
            $('.popup, .overlay').css({'opacity': 0, 'visibility': 'hidden'});
            window.location.href = "/hostedit";
        });
    //    $('.open_window').click(function (e) {
    //        $('.popup, .overlay').css({'opacity': 1, 'visibility': 'visible'});
    //        e.preventDefault();
    //    });
    });
}










function dellAlarm(id) {
    console.log('dellAlarm where id= '+id);
    $.getJSON('/dellAlarm?id='+id+'&userName='+username, function (data) {
        $("#dropdown-menu").empty();
        $("#dropdown-menu-length").empty();
        $("#dropdown-menu-length").append(Object.keys(data).length);
        $.each(data, function (key,values) {
            if(values.type=="error"){
                $("#dropdown-menu").append('<li><a href="#"><b class="label label-warning label-min fa fa-frown-o white-text fa-lg">&nbsp;</b>'+values.message+'' +
                '<b1 idalarm="'+values.id+'"  class="dropdown-menu-right fa fa-times fa-lg"></b1></a></li>');
            }else{
                $("#dropdown-menu").append('<li><a href="#"><b class="label label-success label-min fa fa-smile-o white-text fa-lg">&nbsp;</b>'+values.message+'' +
                '<b1  idalarm="'+values.id+'"  class="dropdown-menu-right fa fa-times fa-lg"></b1></a></li>');
            }
        });
    });
}
function checking() {
        console.log('checking');
        $.getJSON('/getAlarms?userName='+username, function (data) {
            $("#dropdown-menu").empty();
            $("#dropdown-menu-length").empty();
            $("#dropdown-menu-length").append(Object.keys(data).length);
            $.each(data, function (key,values) {
                if(values.type=="error"){
                    $("#dropdown-menu").append('<li><a href="#"><b class="label label-warning label-min fa fa-frown-o white-text fa-lg">&nbsp;</b>'+values.message+'' +
                    '<b1 idalarm="'+values.id+'"  class="dropdown-menu-right fa fa-times fa-lg"></b1></a></li>');
                }else{
                    $("#dropdown-menu").append('<li><a href="#"><b class="label label-success label-min fa fa-smile-o white-text fa-lg">&nbsp;</b>'+values.message+'' +
                    '<b1  idalarm="'+values.id+'"  class="dropdown-menu-right fa fa-times fa-lg"></b1></a></li>');
                }
            });
        });
}
function start_checking() {
    setTimeout(function(){
        console.log('checking');
        $.getJSON('/getAlarms?userName='+username, function (data) {
            $("#dropdown-menu").empty();
            $("#dropdown-menu-length").empty();
            $("#dropdown-menu-length").append(Object.keys(data).length);
            $.each(data, function (key,values) {
                if(values.type=="error"){
                    $("#dropdown-menu").append('<li><a href="#"><b class="label label-warning label-min fa fa-frown-o white-text fa-lg">&nbsp;</b>'+values.message+'' +
                    '<b1 idalarm="'+values.id+'"  class="lol dropdown-menu-right fa fa-times fa-lg"></b1></a></li>');
                }else{
                    $("#dropdown-menu").append('<li><a href="#"><b class="label label-success label-min fa fa-smile-o white-text fa-lg">&nbsp;</b>'+values.message+'' +
                    '<b1 idalarm="'+values.id+'"  class="dropdown-menu-right fa fa-times fa-lg"></b1></a></li>');
                }
            });
        });
        start_checking();
    }, 10000);
}

function setHostName(name) {

    $(document).ready(function () {
        username = name;
        checking();
        start_checking();
    });
}

function hideShowHostListContent(name) {

    $(document).ready(function () {
        username = name;
        checking();
        start_checking();



        $('body').on('click', 'b1', function () {
            //$("#dropdown-menu").empty();
            //$.getJSON('/dellAlarm?id=' + $(this).attr('idalarm'));
            dellAlarm($(this).attr('idalarm'));

        });


        $('.dropdown-toggle').click(function () {
            $("#dropdown-menu").toggle();
        });

        $(document).mouseup(function (e) {
            var container = $("#dropdown-menu");
            if (container.has(e.target).length === 0) {
                container.hide();
            }
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
        console.log(name);

    });
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

