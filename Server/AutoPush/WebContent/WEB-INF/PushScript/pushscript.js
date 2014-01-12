
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
    loginURL : casper.cli.get(2),
    formPath : casper.cli.get(3),
    idPath : casper.cli.get(4),
    passwdPath : casper.cli.get(5),
    id : casper.cli.get(6),
    pwd : casper.cli.get(7)
}



var filename = param.imgPath;

casper.start(param.loginURL, function() {
    this.echo('Home was loaded');
});


casper.then(function() {
   this.echo('Current location is ' + this.getCurrentUrl());
    var values ={};
    this.echo(param.url + ' '+ param.imgPath+ ' '+ param.loginURL + ' '+ param.idPath+' '+ param.passwdPath+' '+ param.id + ' '+ param.pwd);
    values[param.idPath] = param.id;
    values[param.passwdPath] = param.pwd;
    this.echo(values['id']+','+values['pw']);
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
    this.echo('capture location is ' + this.getCurrentUrl());
   this.capture(filename,undefined,
   {
        format: 'jpg',
        quality: 75
   });
   this.echo("Saved screenshot of " + (this.getCurrentUrl()) + " to " + filename);
});
casper.run();