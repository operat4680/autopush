
var utils = require('utils');
var casper = require("casper").create({
  viewportSize: {
     width: 1940,
    height: 1480 },
    pageSettings: {
        loadImages:  true,        // The WebPage instance used by Casper will
        loadPlugins: false,         // use these settings
        webSecurityEnabled: false
    },
    userAgent: 'Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1)'
});
casper.options.waitTimeout = 3000;
var urlList = JSON.parse(casper.cli.get(0).split("-_-").join("\""));
var t = ((casper.cli.get(2)).split("-_-").join("\""));
var transfer=t.split("|").join(" ");
var param = {
    url : urlList.targetUrl,
    receiveUrl : casper.cli.get(1),
    imgPath : transfer,
    userId : casper.cli.get(3),
    loginURL : casper.cli.get(4),
    formPath : casper.cli.get(5),
    idPath : casper.cli.get(6),
    passwdPath : casper.cli.get(7),
    id : casper.cli.get(8),
    pwd : casper.cli.get(9)
}

var req = {};
//var filename = param.imgPath;
if(param.loginURL!=null){
  casper.start(param.loginURL, function() {
      console.log(param.loginURL + ' Home was loaded');
  });



  casper.then(function() {
     console.log('login location is ' + this.getCurrentUrl());
      var values ={};
      console.log(param.receiveUrl+' '+param.url + ' '+ param.loginURL + ' '+ param.idPath+' '+ param.passwdPath+' '+ param.id + ' '+ param.pwd);
      values[param.idPath] = param.id;
      values[param.passwdPath] = param.pwd;
      // console.log(values[param.idPath]+','+values[param.passwdPath]);
      this.fill(param.formPath, values, true);
    });
  


  casper.waitFor(function check() {
      return this.getCurrentUrl() != param.loginURL;
  }, function then() {
      
      console.log('wait!!'+this.getCurrentUrl());
  }, function onTimeout(){
      
  });
  casper.thenOpen(param.url);
}
else{
  casper.start(param.url, function() {
      console.log('Home was loaded2');
  });
}

if(param.imgPath=='not'){
    casper.then(function() {
        var urlArray = urlList.urlList;
        var array = new Array();
        var i=0;
        var test  = new Array('test','test2');
        var tempArray=new Array();
        // console.log('frame count: '+this.page.framesCount);
        casper.then(function(){
          for(var i=0;i<urlArray.length;i++){
              
              tempArray.push(urlArray[i]['url']);
          }
           casper.eachThen(tempArray,function(response){
              console.log('Frame,Iframe List : ' +response.data)
              var temp = {};
              temp['url'] = response.data;
              this.thenOpen(response.data,function(res){
                  // var temp = {};
                  //temp['url'] = res.url;
                  temp['html'] = this.getHTML();
                  array.push(temp);
                
              });
          });

        });

       
        
        casper.then(function(){
          req['htmlData'] = array;
          req['targetUrl'] = param.url;
          req['userId'] = param.userId;
        });
        
        
        // req['htmlData']=this.getHTML();
       
      //   var js = this.evaluate(function() {
      // return document; 
      //   });
        // for(var i=0;i<this.page.framesName.length;i++){
        //   req['htmlData']= req['htmlData']+this.page.frameUrl;
        // }
        // req['htmlData']=this.page.framesCount;
         casper.then(function(){
           if(param.loginURL==null){
             casper.thenOpen(param.receiveUrl,
              {
                method: "post",
                data: {
                  'jsondata' : JSON.stringify(req)
                }
              },
              function() {
             
                console.log( "POST2 request has been sent.");
                // casper.exit();
              });
            }
            else{
              req['loginURL']=param.loginURL;
              req['formPath']=param.formPath;
              req['idPath']=param.idPath;
              req['passwdPath']=param.passwdPath;
              req['id']=param.id;
              req['pwd']=param.pwd;
              casper.thenOpen(param.receiveUrl,
              {
                method: "post",
                data: {
                   'jsondata' : JSON.stringify(req)
                }
              },
              function() {
                console.log( "POST2 login request has been sent.");
              });
            }
        });
    });
    casper.then(function(){
        casper.exit();
    });
}

else{
    
    casper.then(function() {
      var array = new Array();
      var tempArray = new Array();
      var json = JSON.parse(param.imgPath);
      var pathList = json.pathList;
      var count=0;
      

      casper.then(function(){
          for(var i=0;i<pathList.length;i++){ 
              tempArray.push(pathList[i]['url']);
          }
          casper.eachThen(tempArray,function(response){
              console.log('in capture each : ' +response.data);
              temp = response.data;
              this.thenOpen(response.data,function(response){
                var imgPath = pathList[count]['tagList'].split(',');
                for(var i=0;i<imgPath.length;i++){
                   
                    try{
                      if(this.visible(imgPath[i])){
                          var inArray = {};
                          // inArray['url'] = response.url;
                          inArray['url'] = temp;
                          inArray['tagPath'] = imgPath[i];

                          inArray['imgData'] = this.captureBase64('jpg',imgPath[i],{quality:75});
                          
                          console.log(count+','+i+' : '+imgPath[i]);
                          array.push(inArray);
                          
                      }
                    }catch(e){
                          console.log('error');
                         continue;
                    }     
                }
                
                count++;
              });
             
          });
      });
      casper.then(function(){
         req['imgList'] = array;
         req['userId'] = param.userId;
         console.log('img array Size : '+ array.length);
         casper.thenOpen(param.receiveUrl,
          {
            method: "post",
            data: {
               'img' : JSON.stringify(req),
            }
          },
          function() {
             console.log( "ImageData post request has been sent.");
            
          });

      });
      

      

   });
   //   old version 
   // casper.then(function() {
   //    var array = new Array();
   //    var imgPath = param.imgPath.split(',');

   //    for(var i=0;i<imgPath.length;i++){
        
   //      if(this.visible(imgPath[i])){
   //        console.log(i+' : '+imgPath[i]);
   //        var inArray = {};
   //        inArray['tagPath'] = imgPath[i];
   //        try{
   //        inArray['imgData'] = this.captureBase64('png',imgPath[i],{quality:75});
   //        }catch(e){
   //          continue;
   //        }
   //        array.push(inArray);
         
   //      }
   //    }
   //    req['imgList'] = array;

   //    casper.thenOpen(param.receiveUrl,
   //        {
   //          method: "post",
   //          data: {
   //             'imgList' : JSON.stringify(req)
   //          }
   //        },
   //        function() {
   //          // console.log( "POST3 request has been sent.");
   //        });

   // });



}

casper.run();