
var xchart = function(chartOptions){
    this.chartId = chartOptions.divId;   //chart的id
    this.chartType = chartOptions.chartType;   //chart的chartType
    this.titleText = chartOptions.titleText;   //chart的title.text
    this.legendData = chartOptions.legendData;   //chart的legendData
    this.xAxisData = chartOptions.xAxisData;   //chart的xAxisData
    this.seriesData = chartOptions.seriesData;   //chart的seriesData
    this.drawData = chartOptions.drawData;
    return this.init();
};
//初始化
xchart.prototype.init = function(){
	var self = this;
	var myChart = echarts.init(document.getElementById(self.chartId));
	// 使用刚指定的配置项和数据显示图表。
	var option ;
	//异步配置数据
	if(self.drawData){
        var config=self.drawData.echart_config ;
        var data=self.drawData.echart_data ;
        var optionFormatter=self.drawData.echart_option;
    }else{
        var config=document.getElementById(self.chartId).getAttribute("echart-config");
        config=eval('(' + config + ')');
        var data=document.getElementById(self.chartId).getAttribute("echart-data");
        data=eval('(' + data + ')');
        var optionFormatter=document.getElementById(self.chartId).getAttribute("echart-option");
        optionFormatter=eval('(' + optionFormatter + ')');
    }
    var type		= config.type;//图形类型
    var data_type		= config.data_type;//
    var max_key 		= config.max_key;
    var data_name 		= config.data_name;//'分值';
    var x_name_key  	= config.x_name_key;
    var is_stack  		= config.is_stack
    var name_key  		= config.name_key;//x轴名称
    var value_key 		= config.value_key;//展示数据
    var data_key		= config.data_key;//图例名称（多组数据时用的比较多）
    var name_value		= config.name_value;
    var empty_val		= config.empty_val;
    var title		= config.title;
    var flag=false;//用于判断是否为多类图形
    //判断数据展示图形是否为多种
    if(type instanceof Array){
        if(type.length>1){
            flag=true;
        }
    }
    //多类形状数据拼接（饼状图排除）
    if(flag){
        var seriesData 		= [];//数据内容
        var xAxisData       = [];//x轴名称
        var nameKey         = [];//多组数据时，图例名称
        option = optionLine;
        option.legend.data = data_name;
        option.title.text = title;
        if(optionFormatter){
            for(var a in optionFormatter ){
                option.yAxis=optionFormatter.yAxis;
            }
        }
        for(var i in data){
            var one=data[i];
            if(xAxisData .indexOf(one[name_key]) === -1){
                xAxisData.push(one[name_key]);
            }
        }
        option.xAxis.data = xAxisData;
        for(var b in type){
            var datas           = [];
            for(var i in data){
                var one = data[i];
                console.info(one);
                datas.push( {value:one[value_key[b]]} );
            }
            seriesData.push({name:data_name[b], type:type[b],data:datas})
        }
        option.series = seriesData;
    }else{
        if("pie" != type){
            if("line" == type) {
                option = optionLine;
            }
            if("bar" == type) {
                option = optionBar;
            }
            option = optionLine;
            var legendData 		= [];//图例名称
            var seriesData 		= [];//数据内容
            var xAxisData       = [];//x轴名称
            var nameKey         = [];//多组数据时，图例名称
            //用户判断单组还是多组函数
            for(var i in data){
                var one=data[i];
                if(xAxisData .indexOf(one[name_key]) === -1){
                    xAxisData.push(one[name_key]);
                }
                if(data_key&&nameKey .indexOf(one[data_key]) === -1){
                    nameKey.push(one[data_key]);
                }
            }
            if(nameKey.length==0||nameKey.length==1){
                for(var i in data){
                    var one = data[i];
                    legendData.push(one[name_key] );
                    seriesData.push( {value:one[value_key], xLink:one['score_link']} );
                }
                seriesData=[{name:title, type:type,data:seriesData}]
                option.legend.data = legendData;
                option.series = seriesData;
                option.title.text = title;
                option.xAxis.data = xAxisData;
            }else{
                for(var j in nameKey){
                    legendData.push( nameKey[j] );
                    var datas = [];
                    for(var i in data){
                        var one = data[i];
                        if(nameKey[j] ==one[data_key]){
                            datas.push( {value:one[value_key], xLink:one['score_link']} );
                        }

                    }
                    seriesData.push({name:nameKey[j], type:type, itemStyle: itemStyle,data:datas})
                }
                if(optionFormatter){
                    for(var a in optionFormatter ){
                        option.yAxis=optionFormatter.yAxis;
                    }
                }
                option.legend.data = legendData;
                option.series = seriesData;
                //option.yAxis.splitLine.show = false;
                option.title.text = title;
                option.xAxis.data = xAxisData;
            }
        }else if("pie" == type){
            option = optionPie;
            var legendData 		= [];
            var seriesData 		= [];
            for(var i in data){
                var one = data[i];
                legendData.push( one[name_key] );
                seriesData.push( {value:one[value_key], name:one[name_key], xLink:one['score_link']} );
            }
            seriesData=[{name:title, type:type,radius : '55%',
                center: ['50%', '60%'],data:seriesData}]
            option.legend={
                orient: 'vertical',
                left: 'left'
            }
            option.legend.data = legendData;
            option.tooltip={trigger: 'item',
                formatter: "{a} <br/>{b} : {c} ({d}%)"}
            option.series = seriesData;
            option.title.text = title;
            option.title.x='center';
        }else{
            option = optionLine;
        }
    }
	myChart.setOption(option);
    return myChart;
};

//折线图属性
var optionLine = {
        title: {
            text: 'ECharts 入门示例',
            left: 'center',
            textStyle: {
                fontSize: 13
            }
        },
        tooltip: {},
        legend: {
            //type: 'scroll',
            //orient: 'vertical',
            //right: 10,
            top:20,
            //bottom: 20,
            data:['销量']
        },
        xAxis: {
            data: ["衬衫","羊毛衫","雪纺衫","裤子","高跟鞋","袜子"]
        },
        yAxis: {
            splitLine: {
                show: true //不显示网格线
            }
        },
        series: [{
            name: '销量',
            type: 'line',
            data: [5, 20, 36, 10, 10, 20]
        }]
    };

var optionPie = {
        title: {
            text: 'ECharts 入门示例',
            textStyle: {
                fontSize: 13
            }
        },
        tooltip: {},
        legend: {
            data:['销量']
        },
        series: [{
            name: '销量',
            type: 'pie',
            data: [5, 20, 36, 10, 10, 20]
        }]
    };

var optionBar = {
        title: {
            text: 'ECharts 入门示例',
            left: 'center',
            textStyle: {
                fontSize: 13
            }
        },
        tooltip: {},
        legend: {
            //type: 'scroll',
            //orient: 'vertical',
            //right: 10,
            top:20,
            //bottom: 20,
            data:['销量']
        },
        xAxis: {
            data: ["衬衫","羊毛衫","雪纺衫","裤子","高跟鞋","袜子"]
        },
        yAxis: {
            splitLine: {
                show: true //不显示网格线
            }
        },
        series: [{
            name: '销量',
            type: 'bar',
            data: [5, 20, 36, 10, 10, 20],
        }]
    };

var itemStyle = {
        normal: {
            label: {
                show: true,
                    position: 'top',
                    textStyle: {
                    color: 'black'
                }
            }
        }
    };