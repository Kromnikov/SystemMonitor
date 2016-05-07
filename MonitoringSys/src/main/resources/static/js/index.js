var username;

//TODO: Inst Metric
function instMetricPageFunctions() {
    $(document).ready(function () {

        $('body').on('click', '.tohostselect', function () {
            $("#tohostValue").html($(this).attr('hostname'));
            $("#tohostValue").attr('hostId', $(this).attr('hostid'));
        });
        $('body').on('click', '.templMetricselect', function () {
            console.log($(this).attr('metricTitle') + "\n" + $(this).attr('metricQuery'));
            $("input[name='Title']").val($(this).attr('metricTitle'));
            $("input[name='query']").val($(this).attr('metricQuery'));
            $("#templMetricValue").html($(this).attr('metricTitle'));
            $("#templMetricValue").attr('metricId', $(this).attr('metricId'));
            $("#templMetricValue").attr('metricQuery', $(this).attr('metricQuery'));
            $("#templMetricValue").attr('metricTitle', $(this).attr('metricTitle'));
        });

    });

}
function editInstMetric(InstanceMetric,host) {
    $(document).ready(function () {
        $("#addInstMetric").addClass('hidden');
        $("#addInstMetric").addClass('hidden');
        $("#AddInstTitle").addClass('hidden');
        $("#editInstMetric").removeClass('hidden');
        $("#EditInstTitle").removeClass('hidden');

        $("input[name='Title']").val(InstanceMetric.title);
        $("input[name='query']").val(InstanceMetric.command);
        $("input[name='minValue']").val(InstanceMetric.minValue);
        $("input[name='maxValue']").val(InstanceMetric.maxValue);
        $("input[name='id']").val(InstanceMetric.id);


        $.getJSON('/admin/getHostsTempl', function (hostTempl) {
            $("#tohost").empty();
            $("#tohost").append('<li id="tohostMenu"><a required="required" id="tohostValue" href="#"  hostId="' + host.id + '">'+host.name+'</a></li>');
            a = $("#tohostMenu").append('<ul id="tohostnonemenu"></ul>');
            $.each(hostTempl.hosts, function (key, values) {
                console.log(values.id );
                $("#tohostnonemenu").append('<li class="tohostselect" hostname="' + values.name + '" hostId="' + values.id + '"><a href="#">' + values.name + '</a></li>');
            });

            $("#templMetric").empty();
            $("#templMetric").append('<li id="templMetricMenu"><a id="templMetricValue" href="#">'+InstanceMetric.title+'</a></li>');
            a = $("#templMetricMenu").append('<ul id="templMetricnonemenu"></ul>');
            $.each(hostTempl.templateMetrics, function (key, values) {
                $("#templMetricnonemenu").append('<li class="templMetricselect" metricTitle="' + values.title + '" metricQuery="' + values.command + '" metricId="' + values.id + '"><a href="#">' + values.title + '</a></li>');
            });
        });


        //$("#editInstMetric").click(function () {
        //    if ($("#templMetricValue").attr('metricId') != null) {
        //        $.getJSON('/admin/editInstMetric?'
        //            + 'id=' + $("input[name='id']").val()
        //            + '&templId=' + $("#templMetricValue").attr('metricId')
        //            + '&hostId=' + $("#tohostValue").attr('hostId')
        //            + '&title=' + $("input[name='Title']").val()
        //            + '&command=' + $("input[name='query']").val()
        //            + '&minValue=' + $("input[name='minValue']").val()
        //            + '&maxValue=' + $("input[name='maxValue']").val()
        //            , function (host) {
        //                window.location.href = "/alarms";
        //            });
        //    } else {
        //        $.getJSON('/admin/editInstMetric?'
        //            + 'id=' + $("input[name='id']").val()
        //            + '&hostId=' + $("#tohostValue").attr('hostId')
        //            + '&title=' + $("input[name='Title']").val()
        //            + '&command=' + $("input[name='query']").val()
        //            + '&minValue=' + $("input[name='minValue']").val()
        //            + '&maxValue=' + $("input[name='maxValue']").val()
        //            , function (host) {
        //                window.location.href = "/alarms";
        //            });
        //    }
        //});


    });

}
function addInstMetric(host) {
    $(document).ready(function () {
        $.getJSON('/admin/getHostsTempl', function (hostTempl) {
            $("#tohost").empty();
            $("#tohost").append('<li id="tohostMenu"><a required="required" id="tohostValue" href="#"  hostId="' + host.id + '">'+host.name+'</a></li>');
            a = $("#tohostMenu").append('<ul id="tohostnonemenu"></ul>');
            $.each(hostTempl.hosts, function (key, values) {
                $("#tohostnonemenu").append('<li class="tohostselect" hostname="' + values.name + '" hostId="' + values.id + '"><a href="#">' + values.name + '</a></li>');
            });

            $("#templMetric").empty();
            $("#templMetric").append('<li id="templMetricMenu"><a id="templMetricValue" href="#"></a></li>');
            a = $("#templMetricMenu").append('<ul id="templMetricnonemenu"></ul>');
            $.each(hostTempl.templateMetrics, function (key, values) {
                $("#templMetricnonemenu").append('<li class="templMetricselect" metricTitle="' + values.title + '" metricQuery="' + values.command + '" metricId="' + values.id + '"><a href="#">' + values.title + '</a></li>');
            });
        });
        $("#addInstMetric").removeClass('hidden');
        $("AddInstTitle").removeClass('hidden');
        $("#editInstMetric").addClass('hidden');
        $("#EditInstTitle").addClass('hidden');


        $("#addInstMetric").click(function () {
            if ($("#templMetricValue").attr('metricId') != null) {
                $.getJSON('/admin/saveNewInstMetric?'
                    + 'templId=' + $("#templMetricValue").attr('metricId')
                    + '&hostId=' + $("#tohostValue").attr('hostId')
                    + '&title=' + $("input[name='Title']").val()
                    + '&command=' + $("input[name='query']").val()
                    + '&minValue=' + $("input[name='minValue']").val()
                    + '&maxValue=' + $("input[name='maxValue']").val()
                    , function (host) {
                        window.location.href = "/alarms";
                    });
            } else {
                $.getJSON('/admin/saveNewInstMetric?'
                    + 'hostId=' + $("#tohostValue").attr('hostId')
                    + '&title=' + $("input[name='Title']").val()
                    + '&command=' + $("input[name='query']").val()
                    + '&minValue=' + $("input[name='minValue']").val()
                    + '&maxValue=' + $("input[name='maxValue']").val()
                    , function (host) {
                        window.location.href = "/alarms";
                    });
            }
        });


    });
}


//TODO: alarms
function dropDownMenuAlarms() {
    $(document).ready(function () {
        $('.ddmenu').hover(function () {
            clearTimeout($.data(this, 'timer'));
            $('ul', this).stop(true, true).slideDown(200);
        }, function () {
            $.data(this, 'timer', setTimeout($.proxy(function () {
                $('ul', this).stop(true, true).slideUp(200);
            }, this), 100));
        });

    });
}
function addAlarmsModal() {
    $(document).ready(function () {
        //$('.popup.editAlarm, .overlay.editAlarm').css({'opacity': 1, 'visibility': 'visible'});
        $('.openAddAlamr').click(function () {
            $.getJSON('/getNewAlarm', function (alarm) {
                $("#hosts").empty();
                $("#hosts").append('<li id="hostsMenu"><a id="hostsValue" href="#"></a></li>');
                a = $("#hostsMenu").append('<ul id="hostnonemenu"></ul>');
                $.each(alarm.hosts, function (key, values) {
                    $("#hostnonemenu").append('<li class="hostselect" hostid="' + values.id + '" hostname="' + values.name + '"><a href="#">' + values.name + '</a></li>');
                });
                $("#metrics").empty();
                $("#metrics").append('<li id="metricsMenu"><a id="metricValue" href="#"></a></li>');
                a = $("#metricsMenu").append('<ul id="metricsnonemenu"></ul>');
                $.each(alarm.instanceMetrics, function (key, values) {
                    $("#metricsnonemenu").append('<li class="metricselect" metricId="' + values.id + '" metricTitle="' + values.title + '"><a href="#">' + values.title + '</a></li>');
                });
                $("#tohost").empty();
                $("#tohost").append('<li id="tohostMenu"><a id="tohostValue" href="#"></a></li>');
                a = $("#tohostMenu").append('<ul id="tohostnonemenu"></ul>');
                $.each(alarm.hosts, function (key, values) {
                    $("#tohostnonemenu").append('<li class="tohostselect" hostname="' + values.name + '" hostId="' + values.id + '"><a href="#">' + values.name + '</a></li>');
                });
                $("#touser").empty();
                $("#touser").append('<li id="touserMenu"><a id="touservalue" href="#"></a></li>');
                a = $("#touserMenu").append('<ul id="tousernonemenu"></ul>');
                $.each(alarm.allUsers, function (key, values) {
                    $("#tousernonemenu").append('<li class="touserselect" username="' + values + '"><a href="#">' + values + '</a></li>');
                });


                $("#saveAlarm").addClass('hidden');
                $("#addAlarm").removeClass('hidden');

                $('.popup.editAlarm, .overlay.editAlarm').css({'opacity': 1, 'visibility': 'visible'});
                e.preventDefault();
            });
        });


        $("#addAlarm").click(function () {
            $.getJSON('/addAlarm?'
                + 'toEmail=' + $("input[name='toEmail']").val()
                + '&toUser=' + $("#touservalue").html()
                + '&metricId=' + $("#metricValue").attr('metricId')
                + '&hostId=' + $("#tohostValue").attr('hostId')
                + '&user=' + username
                , function (host) {
                    setTimeout(function () {
                        window.location.href = "/alarms";
                    }, 200);
                });
            $('.popup, .overlay').css({'opacity': 0, 'visibility': 'hidden'});
        });
    });
}
function editAlarmsModal() {
    $(document).ready(function () {

        $('.dellAlarm').click(function () {
            $.getJSON('/dellAlarms?id=' + $(this).attr('dellAlarm'), function (alarm) {
                setTimeout(function () {
                    window.location.href = "/alarms";
                }, 200);
            });
        });

        $('body').on('click', '.hostselect', function () {
            var hostId = $(this).attr('hostid');
            $("#hostsValue").html($(this).attr('hostname'));
            $.getJSON('/getInstanceMetrics?hostId=' + hostId, function (instanceMetrics) {
                $("#metrics").empty();
                $("#metrics").append('<li id="metricsMenu"><a id="metricValue" href="#"></a></li>');
                a = $("#metricsMenu").append('<ul id="metricsnonemenu"></ul>');
                $.each(instanceMetrics, function (key, values) {
                    $("#metricsnonemenu").append('<li class="metricselect" metricId="' + values.id + '" metricTitle="' + values.title + '""><a href="#">' + values.title + '</a></li>');
                });
            });
        });
        $('body').on('click', '.metricselect', function () {
            $("#metricValue").html($(this).attr('metricTitle'));
            $("#metricValue").attr('metricId', $(this).attr('metricId'));
        });
        $('body').on('click', '.tohostselect', function () {
            $("#tohostValue").html($(this).attr('hostname'));
            $("#tohostValue").attr('hostId', $(this).attr('hostid'));
        });
        $('body').on('click', '.touserselect', function () {
            $("#touservalue").html($(this).attr('username'));
        });


        //Edit
        $('.open_window').click(function (e) {
            $.getJSON('/getAlarm?id=' + $(this).parent().parent().parent().attr('id'), function (alarm) {
                $("input[name='id']").val(alarm.id);
                $("input[name='toEmail']").val(alarm.toEmail);
                $("#hosts").empty();
                $("#hosts").append('<li id="hostsMenu"><a id="hostsValue" href="#">' + alarm.fromHost + '</a></li>');
                a = $("#hostsMenu").append('<ul id="hostnonemenu"></ul>');
                $.each(alarm.hosts, function (key, values) {
                    $("#hostnonemenu").append('<li class="hostselect" hostid="' + values.id + '" hostname="' + values.name + '"><a href="#">' + values.name + '</a></li>');
                });
                $("#metrics").empty();
                $("#metrics").append('<li id="metricsMenu"><a id="metricValue" href="#">' + alarm.serviceTitle + '</a></li>');
                a = $("#metricsMenu").append('<ul id="metricsnonemenu"></ul>');
                $.each(alarm.instanceMetrics, function (key, values) {
                    $("#metricsnonemenu").append('<li class="metricselect" metricId="' + values.id + '" metricTitle="' + values.title + '"><a href="#">' + values.title + '</a></li>');
                });
                $("#tohost").empty();
                $("#tohost").append('<li id="tohostMenu"><a id="tohostValue" href="#"  hostId="' + alarm.hostId + '">' + alarm.hostName + '</a></li>');
                a = $("#tohostMenu").append('<ul id="tohostnonemenu"></ul>');
                $.each(alarm.hosts, function (key, values) {
                    $("#tohostnonemenu").append('<li class="tohostselect" hostname="' + values.name + '" hostId="' + values.id + '"><a href="#">' + values.name + '</a></li>');
                });
                $("#touser").empty();
                $("#touser").append('<li id="touserMenu"><a id="touservalue" href="#">' + alarm.toUser + '</a></li>');
                a = $("#touserMenu").append('<ul id="tousernonemenu"></ul>');
                $.each(alarm.allUsers, function (key, values) {
                    $("#tousernonemenu").append('<li class="touserselect" username="' + values + '"><a href="#">' + values + '</a></li>');
                });
                $("#saveAlarm").removeClass('hidden');
                $("#addAlarm").addClass('hidden');
                $('.popup.editAlarm, .overlay.editAlarm').css({'opacity': 1, 'visibility': 'visible'});
                e.preventDefault();
            });
        });
        $("#saveAlarm").click(function () {
            $.getJSON('/saveAlarm?id=' + $("input[name='id']").val()
                + '&toEmail=' + $("input[name='toEmail']").val()
                + '&toUser=' + $("#touservalue").html()
                + '&metricId=' + $("#metricValue").attr('metricId')
                + '&hostId=' + $("#tohostValue").attr('hostId')
                , function (host) {
                    setTimeout(function () {
                        window.location.href = "/alarms";
                    }, 200);
                });
            $('.popup, .overlay').css({'opacity': 0, 'visibility': 'hidden'});
        });


        $('.close_window').click(function () {
            $('.popup, .overlay').css({'opacity': 0, 'visibility': 'hidden'});
           // window.location.href = "/alarms";
        });

    });
}


//TODO: Модальные окна редактирования accounts

function dropDownMenuAccounts() {
    $(document).ready(function () {
        $('.ddmenu').hover(function () {
            clearTimeout($.data(this, 'timer'));
            $('ul', this).stop(true, true).slideDown(200);
        }, function () {
            $.data(this, 'timer', setTimeout($.proxy(function () {
                $('ul', this).stop(true, true).slideUp(200);
            }, this), 100));
        });

    });
}
function modalAccounts() {

    $(document).ready(function () {
        $('.openAddAccount').click(function (e) {
            $.getJSON('/admin/getRoles', function (roles) {
                $("#newAccount").empty();
                $("#newAccount").append('<li id="newAccountmainDdmenu"><a id="newAccountRoleValue" href="#"></a></li>');

                $("#newAccountmainDdmenu").append('<ul id="newAccountnoneMenu"></ul>');
                $.each(roles, function (key, values) {
                    $("#newAccountnoneMenu").append('<li class="roleselect" role="' + values + '"><a href="#">' + values + '</a></li>');
                });

                $('.popup.addAccounts, .overlay.addAccounts').css({'opacity': 1, 'visibility': 'visible'});
                e.preventDefault();
            });
        });
        $("#addAccount").click(function () {
            $.getJSON('/admin/addAccount?'
                + '&username=' + $("input[name='NEWUsername']").val()
                + '&password=' + $("input[name='NEWPassword']").val()
                + '&role=' + $("#newAccountRoleValue").html()
                , function (host) {
                });
            $('.popup, .overlay').css({'opacity': 0, 'visibility': 'hidden'});
            setTimeout(function () {
                window.location.href = "/admin/accounts";
            }, 200);
        });


        var a;
        $('body').on('click', '.roleselect', function () {
            var role = $(this).attr('role');
            $("#roleValue").html(role);
            $("#newAccountRoleValue").html(role);
            //$("#roleValue1").html(role);
            //console.log("roleValue -->  "+$("#roleValue").html());
            //console.log("roleValue1 -->  "+$("#roleValue1").html());
        });

        $('.open_window').click(function (e) {
            $.getJSON('/admin/getAccounts?username=' + $(this).parent().parent().parent().attr('id'), function (user) {
                console.log(user.username);
                $("input[name='id']").val(user.id);
                $("input[name='Username']").val(user.username);
                $("input[name='Password']").val(user.password);
                //$("input[name='Role']").val(user.role);

                $(".ddmenu").empty();
                $(".ddmenu").append('<li class="mainDdmenu"><a id="roleValue" href="#">' + user.role + '</a></li>');

                $(".mainDdmenu").append('<ul id="noneMenu"></ul>');
                $.each(user.allRoles, function (key, values) {
                    $("#noneMenu").append('<li class="roleselect" role="' + values + '"><a href="#">' + values + '</a></li>');
                });

                $('.popup.accounts, .overlay.accounts').css({'opacity': 1, 'visibility': 'visible'});
                e.preventDefault();
            });
        });
        $("#saveAccount").click(function () {
            $.getJSON('/admin/saveAccount?id=' + $("input[name='id']").val()
                + '&username=' + $("input[name='Username']").val()
                + '&password=' + $("input[name='Password']").val()
                + '&role=' + $("#roleValue").html()
                , function (host) {
                });
            $('.popup, .overlay').css({'opacity': 0, 'visibility': 'hidden'});
            setTimeout(function () {
                window.location.href = "/admin/accounts";
            }, 200);
        });


        $('.close_window').click(function () {
            $('.popup, .overlay').css({'opacity': 0, 'visibility': 'hidden'});
           // window.location.href = "/accounts";
        });

    });
}


//TODO: Модальные окна редактирования templ
function loadPageWithtModal(templMetric) {
    $(document).ready(function () {
            $("input[name='id']").val(templMetric.id);
            $("input[name='Title']").val(templMetric.title);
            $("input[name='Command']").val(templMetric.command);
            $("input[name='Min Value']").val(templMetric.minValue);
            $("input[name='Max Value']").val(templMetric.maxValue);

            $('.popup.templMetric, .overlay.templMetric').css({'opacity': 1, 'visibility': 'visible'});
            e.preventDefault();
    });
}
function loadPageWithoutModal() {

    $(document).ready(function () {
        $('.open_window').click(function (e) {
            $.getJSON('/admin/getTemplMetric?metricId=' + $(this).parent().parent().parent().attr('id'), function (templMetric) {
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
    });
}
function modalTemplMetirc() {

    $(document).ready(function () {

        //dell
        $('.dellTemplMetric').click(function () {
            $.getJSON('/admin/dellTemplMetric?metricId=' + $(this).parent().parent().attr('id'), function (templMetric) {

            });
        });


        //Add
        $('.openAddTemplate').click(function (e) {
            $('.popup.addtemplMetric, .overlay.addtemplMetric').css({'opacity': 1, 'visibility': 'visible'});
            e.preventDefault();
        });
        $("#addTemplMetric").click(function () {
            $.getJSON('/admin/addTemplMetric?id=' + $("input[name='sid']").val()
                + '&title=' + $("input[name='sTitle']").val()
                + '&command=' + $("input[name='sCommand']").val()
                + '&minValue=' + $("input[name='sMin Value']").val()
                + '&maxValue=' + $("input[name='sMax Value']").val()
                , function (host) {
                });
            $('.popup, .overlay').css({'opacity': 0, 'visibility': 'hidden'});

            setTimeout(function () {
                window.location.href = "/admin/templMetrics";
            }, 200);
        });


        //Edit

        $("#saveTemplMetric").click(function () {
            $.getJSON('/admin/saveTemplMetric?id=' + $("input[name='id']").val()
                + '&title=' + $("input[name='Title']").val()
                + '&command=' + $("input[name='Command']").val()
                + '&minValue=' + $("input[name='Min Value']").val()
                + '&maxValue=' + $("input[name='Max Value']").val()
                , function (host) {
                });
            $('.popup, .overlay').css({'opacity': 0, 'visibility': 'hidden'});
            setTimeout(function () {
                window.location.href = "/admin/templMetrics";
            }, 200);
        });


        $('.close_window').click(function () {
            $('.popup, .overlay').css({'opacity': 0, 'visibility': 'hidden'});
           // window.location.href = "/templMetrics";
        });

    });
}


//TODO: Модальные окна редактирования хостов

//$("#tohostValue").html($(this).attr('hostname'));
//$("#tohostValue").attr('hostId', $(this).attr('hostid'));
function clearModal() {
    $("input[name='Title']").val('');
    $("input[name='query']").val('');
    $("input[name='minValue']").val('');
    $("input[name='maxValue']").val('');

}
function closeModal(hostId) {

    $("#InstanceMetric").empty();

    $.getJSON('/admin/getMetricsRow?hostid=' + hostId, function (metrics) {
        $.each(metrics.instanceMetrics, function (key, values) {
            $("#InstanceMetric").append('<tr><td class="cursor_pointer editInstMetric"  hostId="' + metrics.hostId + '" instMetricId="' + values.id + '" >' + values.title + '</td><td><instance hostId="' + metrics.hostId + '" instMetricId="' + values.id + '" class="fa fa-times fa-lg hovercolorredtext hovercursor"></instance></td></tr>');
        });
    });


    $('.popup1.editInst, .overlay1.editInst').animate({opacity: "0"}, 100);
    $('.popup.services').animate({left: "50%"}, 500);
}
function modalInst() {
    $(document).ready(function () {
        $('.close_window1').click(function () {
            $('.popup1.editInst, .overlay1.editInst').animate({opacity: "0"}, 100);
            $('.popup.services').animate({left: "50%"}, 500);
        });
        $('#cancel').click(function () {
            $('.popup1.editInst, .overlay1.editInst').animate({opacity: "0"}, 100);
            $('.popup.services').animate({left: "50%"}, 500);
        });

        $('body').on('click', '#addInsetMetricPage', function () {
            clearModal();
            $.getJSON('/getHost?id=' + $(this).attr('hostId'), function (host) {
                $.getJSON('/admin/getHostsTempl', function (hostTempl) {
                    $("#tohost").empty();
                    $("#tohost").append('<li id="tohostMenu"><a required="required" id="tohostValue" href="#"  hostId="' + host.id + '">' + host.name + '</a></li>');
                    a = $("#tohostMenu").append('<ul id="tohostnonemenu"></ul>');
                    $.each(hostTempl.hosts, function (key, values) {
                        $("#tohostnonemenu").append('<li class="tohostselect" hostname="' + values.name + '" hostId="' + values.id + '"><a href="#">' + values.name + '</a></li>');
                    });

                    $("#templMetric").empty();
                    $("#templMetric").append('<li id="templMetricMenu"><a id="templMetricValue" href="#"></a></li>');
                    a = $("#templMetricMenu").append('<ul id="templMetricnonemenu"></ul>');
                    $.each(hostTempl.templateMetrics, function (key, values) {
                        $("#templMetricnonemenu").append('<li class="templMetricselect" metricTitle="' + values.title + '" metricQuery="' + values.command + '" metricId="' + values.id + '"><a href="#">' + values.title + '</a></li>');
                    });
                });
            });
            $("#addInstMetric").removeClass('hidden');
            $("AddInstTitle").removeClass('hidden');
            $("#editInstMetric").addClass('hidden');
            $("#EditInstTitle").addClass('hidden');



            $('.popup.services').animate({left: "-4%"}, 500);
            $('.popup1.editInst, .overlay1.editInst').css({'visibility': 'visible'});
            $('.popup1.editInst, .overlay1.editInst').animate({opacity: "1"}, 500);
        });

        $("#addInstMetric").click(function () {
            if ($("#templMetricValue").attr('metricId') != null) {
                $.getJSON('/admin/saveNewInstMetric?'
                    + 'templId=' + $("#templMetricValue").attr('metricId')
                    + '&hostId=' + $("#tohostValue").attr('hostId')
                    + '&title=' + $("input[name='Title']").val()
                    + '&command=' + $("input[name='query']").val()
                    + '&minValue=' + $("input[name='minValue']").val()
                    + '&maxValue=' + $("input[name='maxValue']").val()
                    , function (host) {
                        //window.location.href = "/alarms";
                    });
                setTimeout(function () {
                    closeModal($("#tohostValue").attr('hostId'));
                    clearModal();
                }, 300);
            } else {
                $.getJSON('/admin/saveNewInstMetric?'
                    + 'hostId=' + $("#tohostValue").attr('hostId')
                    + '&title=' + $("input[name='Title']").val()
                    + '&command=' + $("input[name='query']").val()
                    + '&minValue=' + $("input[name='minValue']").val()
                    + '&maxValue=' + $("input[name='maxValue']").val()
                    , function (host) {
                        //window.location.href = "/alarms";
                    });
                setTimeout(function () {
                    closeModal($("#tohostValue").attr('hostId'));
                    clearModal();
                }, 300);
            }
        });

        $('body').on('click', '.editInstMetric', function () {
            $.getJSON('/admin/getInstTempHost?instMetricId=' + $(this).attr('instMetricId'), function (row) {

                    $("#addInstMetric").addClass('hidden');
                    $("#addInstMetric").addClass('hidden');
                    $("#AddInstTitle").addClass('hidden');
                    $("#editInstMetric").removeClass('hidden');
                    $("#EditInstTitle").removeClass('hidden');

                    $("input[name='Title']").val(row.instanceMetrics.title);
                    $("input[name='query']").val(row.instanceMetrics.command);
                    $("input[name='minValue']").val(row.instanceMetrics.minValue);
                    $("input[name='maxValue']").val(row.instanceMetrics.maxValue);
                    $("input[name='id']").val(row.instanceMetrics.id);


                    $.getJSON('/admin/getHostsTempl', function (hostTempl) {
                        $("#tohost").empty();
                        $("#tohost").append('<li id="tohostMenu"><a required="required" id="tohostValue" href="#"  hostId="' + row.host.id + '">' + row.host.name + '</a></li>');
                        a = $("#tohostMenu").append('<ul id="tohostnonemenu"></ul>');
                        $.each(hostTempl.hosts, function (key, values) {
                            $("#tohostnonemenu").append('<li class="tohostselect" hostname="' + values.name + '" hostId="' + values.id + '"><a href="#">' + values.name + '</a></li>');
                        });

                        $("#templMetric").empty();
                        $("#templMetric").append('<li id="templMetricMenu"><a id="templMetricValue" href="#" metricId="'+row.templateMetrics.id+'">' + row.templateMetrics.title + '</a></li>');
                        a = $("#templMetricMenu").append('<ul id="templMetricnonemenu"></ul>');
                        $.each(hostTempl.templateMetrics, function (key, values) {
                            $("#templMetricnonemenu").append('<li class="templMetricselect" metricTitle="' + values.title + '" metricQuery="' + values.command + '" metricId="' + values.id + '"><a href="#">' + values.title + '</a></li>');
                        });
                    });

                });

            $('.popup.services').animate({left: "-4%"}, 500);
            $('.popup1.editInst, .overlay1.editInst').css({'visibility': 'visible'});
            $('.popup1.editInst, .overlay1.editInst').animate({opacity: "1"}, 500);
        });

        $("#editInstMetric").click(function () {
            if ($("#templMetricValue").attr('metricId') != null) {
                $.getJSON('/admin/editInstMetric?'
                    + 'id=' + $("input[name='id']").val()
                    + '&templId=' + $("#templMetricValue").attr('metricId')
                    + '&hostId=' + $("#tohostValue").attr('hostId')
                    + '&title=' + $("input[name='Title']").val()
                    + '&command=' + $("input[name='query']").val()
                    + '&minValue=' + $("input[name='minValue']").val()
                    + '&maxValue=' + $("input[name='maxValue']").val()
                    , function (host) {

                    });
                setTimeout(function () {
                    closeModal($("#tohostValue").attr('hostId'));
                    clearModal();
                }, 300);
            } else {
                $.getJSON('/admin/editInstMetric?'
                    + 'id=' + $("input[name='id']").val()
                    + '&hostId=' + $("#tohostValue").attr('hostId')
                    + '&title=' + $("input[name='Title']").val()
                    + '&command=' + $("input[name='query']").val()
                    + '&minValue=' + $("input[name='minValue']").val()
                    + '&maxValue=' + $("input[name='maxValue']").val()
                    , function (host) {

                    });
                setTimeout(function () {
                    closeModal($("#tohostValue").attr('hostId'));
                    clearModal();
                }, 300);
            }
        });
    });
}
function modalEditHostMetrics() {
    $(document).ready(function () {
        //$('body').on('click', '.editInstMetric', function () {
        //    window.location.href = "/instMetric?instMetricId=" + $(this).attr('instMetricId');
        //});
        $('body').on('click', '.editTemplMetric', function () {
            window.location.href = "/templMetrics?id=" + $(this).attr('templMetricId');
        });

        //$('body').on('click', '#addInsetMetricPage', function () {
        //    window.location.href = "/instMetric?hostId=" + $(this).attr('hostId');
        //});

        $('.open_inst_metrics').click(function (e) {
            $('#addInsetMetricPage').attr("hostId",$(this).parent().parent().parent().attr('id'));



            $("#InstanceMetric").empty();
            $("#TemplateMetric").empty();
            $.getJSON('/admin/getMetricsRow?hostid=' + $(this).parent().parent().parent().attr('id'), function (metrics) {
                $.each(metrics.instanceMetrics, function (key, values) {
                    $("#InstanceMetric").append('<tr><td class="cursor_pointer editInstMetric"  hostId="' + metrics.hostId + '" instMetricId="' + values.id + '" >' + values.title + '</td><td><instance hostId="' + metrics.hostId + '" instMetricId="' + values.id + '" class="fa fa-times fa-lg hovercolorredtext hovercursor"></instance></td></tr>');
                });
                $.each(metrics.templateMetrics, function (key, values) {
                    $("#TemplateMetric").append('<tr><td class="cursor_pointer editTemplMetric" hostId="' + metrics.hostId + '" templMetricId="' + values.id + '">' + values.title + '</td><td><template hostId="' + metrics.hostId + '" templMetricId="' + values.id + '" class="fa fa-plus fa-lg hovercolorgreentext hovercursor"></template></td></tr>');
                });
            });
            $('.popup.services, .overlay.services').css({'opacity': 1, 'visibility': 'visible'});
            $('.popup.services').css({'left': '50%'});
            $('.popup.services').css({'width': '700px'});


            $('body').on('click', 'template', function () {
                console.log($(this).attr('templMetricId'));
                $("#InstanceMetric").empty();
                $.getJSON('/admin/moveToInstMetric?hostid=' + $(this).attr('hostId') + '&templMetricid=' + $(this).attr('templMetricid'), function (metrics) {
                    $.each(metrics.instanceMetrics, function (key, values) {
                        $("#InstanceMetric").append('<tr><td  class="cursor_pointer editInstMetric"  hostId="' + metrics.hostId + '" instMetricId="' + values.id + '">' + values.title + '</td><td><instance hostId="' + metrics.hostId + '" instMetricId="' + values.id + '" class="fa fa-times fa-lg hovercolorredtext hovercursor"></instance></td></tr>');
                    });
                });
            });


            $('body').on('click', 'instance', function () {
                console.log($(this).attr('instMetricId'));
                $("#InstanceMetric").empty();
                $.getJSON('/admin/moveFromInstMetric?hostid=' + $(this).attr('hostId') + '&instMetricid=' + $(this).attr('instMetricId'), function (metrics) {
                    $.each(metrics.instanceMetrics, function (key, values) {
                        $("#InstanceMetric").append('<tr><td  class="cursor_pointer editInstMetric"  hostId="' + metrics.hostId + '" instMetricId="' + values.id + '">' + values.title + '</td><td><instance hostId="' + metrics.hostId + '" instMetricId="' + values.id + '" class="fa fa-times fa-lg hovercolorredtext hovercursor"></instance></td></tr>');
                    });
                });
            });
        });

    });

}
function modalEditHost() {

    $(document).ready(function () {
        $('.openAddHost').click(function (e) {
            $('.popup.services').css({'left': '50%'});
                $('.popup.addhost, .overlay.addhost').css({'opacity': 1, 'visibility': 'visible'});
                e.preventDefault();
        });
        $("#addHost").click(function () {
            $.getJSON('/admin/addHost?'
                + 'name=' + $("input[name='NEWname']").val()
                + '&host=' + $("input[name='NEWhost']").val()
                + '&port=' + $("input[name='NEWport']").val()
                + '&login=' + $("input[name='NEWlogin']").val()
                + '&password=' + $("input[name='NEWpassword']").val()
                + '&location=' + $("input[name='NEWlocation']").val()
                , function (host) {
                });
            $('.popup, .overlay').css({'opacity': 0, 'visibility': 'hidden'});
            setTimeout(function () {
                window.location.href = "/admin/hostedit";
            }, 200);
        });




        $('.open_window').click(function (e) {
            $.getJSON('/admin/gethost?hostid=' + $(this).parent().parent().parent().attr('id'), function (host) {
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
            $.getJSON('/admin/saveHost?id=' + $("input[name='id']").val()
                + '&name=' + $("input[name='name']").val()
                + '&host=' + $("input[name='host']").val()
                + '&port=' + $("input[name='port']").val()
                + '&login=' + $("input[name='login']").val()
                + '&password=' + $("input[name='password']").val()
                + '&location=' + $("input[name='location']").val()
                , function (host) {
                });
            $('.popup, .overlay').css({'opacity': 0, 'visibility': 'hidden'});
            setTimeout(function () {
                window.location.href = "/admin/hostedit";
            }, 200);
        });

        //$('.popup .close_window, .overlay').click(function (){
        $('.close_window').click(function () {
            $('.popup, .overlay').css({'opacity': 0, 'visibility': 'hidden'});
            $('.popup1.editInst, .overlay1.editInst').css({'opacity': 0, 'visibility': 'hidden'});
            setTimeout(function () {
                window.location.href = "/admin/hostedit";
            }, 200);
        });
    });
}









































function dellAlarm(id) {
    console.log('dellAlarm where id= ' + id);
    $.getJSON('/dellAlarm?id=' + id + '&userName=' + username, function (data) {
        $("#dropdown-menu").empty();
        $("#dropdown-menu-length").empty();
        $("#dropdown-menu-length").append(Object.keys(data).length);
        $.each(data, function (key, values) {
            if (values.type == "error") {
                $("#dropdown-menu").append('<li><a href="#"><b class="label label-warning label-min fa fa-frown-o white-text fa-lg">&nbsp;</b>' + values.message + '' +
                '<b1 idalarm="' + values.id + '"  class="dropdown-menu-right fa fa-times fa-lg"></b1></a></li>');
            } else {
                $("#dropdown-menu").append('<li><a href="#"><b class="label label-success label-min fa fa-smile-o white-text fa-lg">&nbsp;</b>' + values.message + '' +
                '<b1  idalarm="' + values.id + '"  class="dropdown-menu-right fa fa-times fa-lg"></b1></a></li>');
            }
        });
    });
}
function checking() {
    //console.log('checking');
    $.getJSON('/getAlarms?userName=' + username, function (data) {
        $("#dropdown-menu").empty();
        $("#dropdown-menu-length").empty();
        $("#dropdown-menu-length").append(Object.keys(data).length);
        $.each(data, function (key, values) {
            if (values.type == "error") {
                $("#dropdown-menu").append('<li><a href="#"><b class="label label-warning label-min fa fa-frown-o white-text fa-lg">&nbsp;</b>' + values.message + '' +
                '<b1 idalarm="' + values.id + '"  class="dropdown-menu-right fa fa-times fa-lg"></b1></a></li>');
            } else {
                $("#dropdown-menu").append('<li><a href="#"><b class="label label-success label-min fa fa-smile-o white-text fa-lg">&nbsp;</b>' + values.message + '' +
                '<b1  idalarm="' + values.id + '"  class="dropdown-menu-right fa fa-times fa-lg"></b1></a></li>');
            }
        });
    });
}
function start_checking() {
    setTimeout(function () {
        //console.log('checking');
        $.getJSON('/getAlarms?userName=' + username, function (data) {
            $("#dropdown-menu").empty();
            $("#dropdown-menu-length").empty();
            $("#dropdown-menu-length").append(Object.keys(data).length);
            $.each(data, function (key, values) {
                if (values.type == "error") {
                    $("#dropdown-menu").append('<li><a href="#"><b class="label label-warning label-min fa fa-frown-o white-text fa-lg">&nbsp;</b>' + values.message + '' +
                    '<b1 idalarm="' + values.id + '"  class="lol dropdown-menu-right fa fa-times fa-lg"></b1></a></li>');
                } else {
                    $("#dropdown-menu").append('<li><a href="#"><b class="label label-success label-min fa fa-smile-o white-text fa-lg">&nbsp;</b>' + values.message + '' +
                    '<b1 idalarm="' + values.id + '"  class="dropdown-menu-right fa fa-times fa-lg"></b1></a></li>');
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
        //console.log(name);

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

