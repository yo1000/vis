/**
 * Created by yoichi.kikuchi on 15/06/09.
 *
 * depends on d3.js, c3.js
 */
var renderLineChart = function(bindto, data) {
    if (data.length <= 0) {
        return;
    }

    var first = data[0];
    var cols = new Array();
    var keys = new Array();

    for (var col in first) {
        if (cols.length > 0) {
            keys.push(col);
        }
        cols.push(col);
    }

    var chart = c3.generate({
        bindto: bindto,
        data: {
            json: data,
            keys: {
                x: cols[0],
                value: keys
            }
        },
        axis: {
            x: {
                show: (data.length <= 24),
                type: 'category',
                tick: {
                    rotate: 90,
                    multiline: false
                },
                height: 60
            }
        },
        point: {
            show: false
        }
    });
};

var renderStackedBarChartFromArray = function(bindto, data) {
    if (data.length <= 0) {
        return;
    }

    var cols = new Array();
    for (var i = 0; i < data.length; i++) {
        cols.push(String(data[i][0]));
    }

    var chart = c3.generate({
        bindto: bindto,
        data: {
            columns: data,
            type: 'bar',
            groups: [
                cols
            ],
            order: function(a, b) {
                return Number(a.id) - Number(b.id);
            }
        },
        axis: {
            x: {
                type: 'category',
                categories: cols,
                tick: {
                    rotate: 90,
                    multiline: false
                },
                height: 60
            }
        },
        grid: {
            y: {
                lines: [{value:0}]
            }
        }
    });
};

var renderTable = function(bindto, data, reverse) {
    var length = arguments.length;
    var rev = length < 3 ? true : reverse;

    if (data.length <= 0) {
        return;
    }

    var first = data[0];
    var cols = new Array();

    for (var col in first) {
        cols.push(col);
    }

    var $table = $('<table class="table table-hover table-condensed"></table>');
    var $tableHead = $('<thead></thead>');
    var $trHead = $('<tr></tr>');
    $table.append($tableHead);

    for (var index in cols) {
        $trHead.append('<th>' + cols[index] + '</th>');
    }

    $tableHead.append($trHead);

    var $tableBody = $('<tbody></tbody>');
    $table.append($tableBody);

    for (var key in data) {
        var d = data[key];
        var $trData = $('<tr></tr>');

        for (var index in cols) {
            var value = d[cols[index]];

            if (index > 0) {
                if (String(value).match(/^-?[0-9]+\.[0-9]+$/)) {
                    value = Number(value).toFixed(2);
                }
                else if (String(value).match(/^-?[0-9]+$/)) {
                    value = String(value).replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,");
                }
            }

            $trData.append('<td>' + value + '</td>');
        }

        if (rev) {
            $tableBody.prepend($trData);
        }
        else {
            $tableBody.append($trData);
        }
    }

    $(bindto).append($table);
};

var renderTableFromArray = function(bindto, data) {
    if (data.length <= 0) {
        return;
    }

    var first = data[0];
    var cols = new Array();
    cols.push('');

    for (var i = 0; i < data.length; i++) {
        cols.push(data[i][0]);
    }
    cols.push('');

    var $table = $('<table class="table table-hover table-condensed"></table>');
    var $tableHead = $('<thead></thead>');
    var $trHead = $('<tr></tr>');
    $table.append($tableHead);

    for (var i = 0; i < cols.length; i++) {
        $trHead.append('<th>' + cols[i] + '</th>');
    }
    $tableHead.append($trHead);

    var $tableBody = $('<tbody></tbody>');
    $table.append($tableBody);

    for (var i = 0; i < data.length; i++) {
        var d = data[i];
        var $trData = $('<tr></tr>');

        for (var j = 0; j < d.length; j++) {
            var value = d[j];

            if (j > 0) {
                if (String(value).match(/^-?[0-9]+\.[0-9]+$/)) {
                    value = Number(value).toFixed(2);
                }
                else if (String(value).match(/^-?[0-9]+$/)) {
                    value = String(value).replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,");
                }
            }

            if (j === 0) {
                $trData.append('<th>' + value + '</th>');
                continue;
            }

            $trData.append('<td>' + value + '</td>');

            if (j === data.length) {
                $trData.append('<th>' + cols[i + 1] + '</th>');
            }
        }

        $tableBody.append($trData);
    }

    var $tableFoot = $('<tfoot></tfoot>');
    var $trFoot = $('<tr></tr>');
    $table.append($tableFoot);

    for (var i = 0; i < cols.length; i++) {
        $trFoot.append('<th>' + cols[i] + '</th>');
    }
    $tableFoot.append($trFoot);

    $(bindto).append($table);
}
