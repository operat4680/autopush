
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
var param = {
    url : urlList.targetUrl,
    receiveUrl : casper.cli.get(1),
    imgPath : casper.cli.get(2),
    userId : casper.cli.get(3),
    loginURL : casper.cli.get(4),
    formPath : casper.cli.get(5),
    idPath : casper.cli.get(6),
    passwdPath : casper.cli.get(7),
    pPath : casper.cli.get(8),
    id : casper.cli.get(9),
    pwd : casper.cli.get(10)
}
var dataList = {};
var req = {};
//var filename = param.imgPath;
if(param.loginURL!=null){
  casper.start(param.loginURL, function() {
      console.log(param.loginURL + ' Home was loaded');
  });



  casper.then(function() {
     console.log('login location is ' + this.getCurrentUrl());
      var values ={};
      console.log(param.receiveUrl+' '+param.url + ' '+ param.loginURL + ' '+ param.idPath+' '+ param.passwdPath);
      values[param.idPath] = param.id;
      values[param.passwdPath] = param.pwd;
      // console.log(values[param.idPath]+','+values[param.passwdPath]);
       try{ 
        this.fill(param.formPath, values, true);
      }catch(e){

      }
    });
  


  casper.waitFor(function check() {
      return this.getCurrentUrl() != param.loginURL;
  }, function then() {
      
      console.log('wait!!'+this.getCurrentUrl());
  }, function onTimeout(){

      casper.then(function() {
        var values ={};
        values[param.idPath] = param.id;
        values[param.passwdPath] = param.pwd;
         try{      
        this.fill(param.formPath, values, false);
        }catch(e){
                //do nothing
        }
      });
      casper.then(function(){
        try{
        casper.sendKeys(param.pPath,'',{keepFocus:true});
        casper.sendKeys(param.pPath,casper.page.event.key.Enter);
        }catch(e){

        }
      });
      casper.waitFor(function check() {
         return this.getCurrentUrl() != param.loginURL;
      },function then(){

      },function timeout(){

      });
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
              req['pPath']=param.pPath;
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
}

casper.thenOpen(param.receiveUrl,
          {
            method: "post",
            data: {
              'getData' : param.userId
            }
          },
          function() {
            
          }).then(function(){

            dataList = JSON.parse(this.getPageContent());


          });;
   
    
  casper.then(function() {
      var array = new Array();
      var tempArray = new Array();
      var json = dataList;
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

                          inArray['imgData'] = this.captureBase64('jpg',imgPath[i],{quality:30});
                          
                          // console.log(count+','+i+' : '+imgPath[i]);
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
             casper.exit();
            
          });

      });
      

      

   });
  

casper.run();