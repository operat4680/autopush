var fs = require('fs');
var logfilename= 'D://casperjs/captureErrorLog.txt';
function UserException (message) {
  this.message=message;
  this.name="nonVisible";
}
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
casper.options.waitTimeout = 2000;

var input = {
  receiveUrl : casper.cli.get(0),
  queueId : casper.cli.get(1)
};
var req = {};
//connect and getData
var data = {};
var array = new Array();
var alldata;


casper.start();

casper.thenOpen(input.receiveUrl,
          {
            method: "post",
            data: {
                'op' : "get",
               'queueid' : input.queueId
            }
          }
).then(function(){
  data = JSON.parse(this.getPageContent());
 
});
casper.then(function(){
  var count=0;
  casper.eachThen(data.taskList,function(response){
      if(response.data!=null){
      count++;
      var url = response.data.targetUrl;
      var taskId = response.data._id.$oid
      var tagPath = response.data.tagPath;
      var loginURL = response.data.loginURL;
      var idPath = response.data.idPath;
      var passwdPath = response.data.passwdPath;
      var id = response.data.id;
      var pwd = response.data.pwd;
      var formPath = response.data.formPath;
      var pPath = response.data.pPath;
      var temp={};
      if(loginURL!=null){
          casper.thenOpen(loginURL).then(function() {
             console.log(url+' '+loginURL);
             console.log('login location is ' + this.getCurrentUrl());
              var values ={};
              values[idPath] = id;
              values[passwdPath] =pwd;
              try{
              this.fill(formPath, values, true);
              }catch(e){
                
              }
          }).waitFor(function check() {
              return this.getCurrentUrl() != loginURL;
          }, function then() {

          }, function onTimeout(){
              casper.then(function() {
              var values ={};
              values[idPath] = id;
              values[passwdPath] =pwd;
              try{
              this.fill(formPath, values, false);
              }catch(e){
                //do nothing
              }
              }).then(function(){
                  try{
                  casper.sendKeys(pPath,'',{keepFocus:true});
                  casper.sendKeys(pPath,casper.page.event.key.Enter);
                  }catch(e){
          
                  }
              }).waitFor(function check() {
                   return this.getCurrentUrl() != loginURL;
              }, function then() {

              }, function onTimeout(){
              
              });
              
          });
          casper.thenOpen(url).then(function(){
              // console.log('hi');
              // console.log(url);
              // console.log(taskId);
              // console.log(tagPath);
              temp['html'] = this.getHTML();
              try{
               if(this.visible(tagPath)){
                // this.captureSelector('D://casperjs/1'+count+'.jpg',tagPath);
                temp['imgData'] = this.captureBase64('jpg',tagPath,{quality:30});
                }
                else{
                  visibleException =new UserException("nonVisible");
                  throw visibleException;
                } 
              }catch(e){
                console.log('capture Error');

              }
              temp['id'] = taskId;
              temp['tagPath']=tagPath;
          });
      }
      else{
        casper.thenOpen(url).then(function(){
              // console.log('hi2');
              // console.log(url);
              // console.log(taskId);
              // console.log(tagPath);
              temp['html'] = this.getHTML();
              try{
                if(this.visible(tagPath)){
                // this.captureSelector('D://casperjs/1'+count+'.jpg',tagPath);
                temp['imgData'] = this.captureBase64('jpg',tagPath,{quality:30});
                }
                else{
                  visibleException =new UserException("nonVisible");
                  throw visibleException;
                } 
              }catch(e){
                console.log('capture Error');
              }
              temp['id'] = taskId;
              temp['tagPath']=tagPath;
          });
      }
      casper.then(function(){
          array.push(temp);
      });
    }
  });
  
});
casper.then(function(){
  console.log('length : '+ array.length);
  console.log('input : ' + input.queueId);
  casper.then(function(){
    req['taskData'] = array;
  });
  
  casper.then(function(){
      alldata = JSON.stringify(req);
      // casper.waitFor(function asdf() {
      //         return alldata!=null;
      //     }, function then() {

      //     }, function onTimeout(){
      //         console.log('timeout!');
      // });

      casper.thenOpen(input.receiveUrl,
          {
            method: "post",
            data: {
                'op' : "update",
               'queueid' : input.queueId,
                'resultData' : alldata
            }
          }
      ).then(function(){
        console.log(alldata.length);
        console.log('exit :');
        casper.exit();
      });  
  });

 

});

casper.run();