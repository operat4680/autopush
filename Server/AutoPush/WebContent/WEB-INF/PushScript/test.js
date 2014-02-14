
var casper = require("casper").create({
  viewportSize: {
    width: 1024,
    height: 768  },
    pageSettings: {
        loadImages:  true,        // The WebPage instance used by Casper will
        loadPlugins: true         // use these settings
    },
    userAgent: 'Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1)'
});
casper.options.waitTimeout = 3000;

var param = {
    url : casper.cli.get(0),
    imgPath : casper.cli.get(1),
    receiveUrl : casper.cli.get(2),
    loginURL : casper.cli.get(3),
    formPath : casper.cli.get(4),
    idPath : casper.cli.get(5),
    passwdPath : casper.cli.get(6),
    id : casper.cli.get(7),
    pwd : casper.cli.get(8)
}

var req = {};
req['urlPath']='123';
var filename = param.imgPath;

casper.start(param.loginURL, function() {
    this.echo('Home was loaded');
});


casper.then(function() {
   this.echo('Current location is ' + this.getCurrentUrl());
    var values ={};
    this.echo(param.receiveUrl+' '+param.url + ' '+ param.imgPath+ ' '+ param.loginURL + ' '+ param.idPath+' '+ param.passwdPath+' '+ param.id + ' '+ param.pwd);
    values[param.idPath] = param.id;
    values[param.passwdPath] = param.pwd;
    this.echo(values[param.idPath]+','+values[param.passwdPath]);
    this.fill(param.formPath, values, true);
});


casper.waitFor(function check() {
    return this.getCurrentUrl() != param.url;
}, function then() {
    
    console.log('wait!!'+this.getCurrentUrl());
}, function onTimeout(){
    
});

casper.thenOpen(param.url);
casper.then(function() {
    req['urlPath']=this.getHTML();
    this.echo('capture location is ' + this.getCurrentUrl());
   this.capture(filename,undefined,
   {
        format: 'jpg',
        quality: 75
   });
   this.echo("Saved screenshot of " + (this.getCurrentUrl()) + " to " + filename);

   // casper.thenOpen(param.receiveUrl,
   //  {
   //    method: "post",
   //    data: {
   //      'urlPath' : req['urlPath']
   //    }
   //  },
   //  function() {
   
   //    this.echo( "POST request has been sent.");
   //    // casper.exit();
   //  });
});





casper.run();