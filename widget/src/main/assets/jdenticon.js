// Jdenticon 1.7.2 | jdenticon.com | zlib licensed | (c) 2014-2017 Daniel Mester Pirttijärvi
(function(t,v,q){var m=q(t,t.jQuery);"undefined"!==typeof module&&"exports"in module?module.exports=m:"function"===typeof define&&define.amd?define([],function(){return m}):t[v]=m})(this,"jdenticon",function(t,v){function q(a,b){this.x=a;this.y=b}function m(a,b,c,d){this.o=a;this.s=b;this.D=c;this.j=d}function G(a){this.C=a;this.m=m.O}function u(a){a|=0;return 0>a?"00":16>a?"0"+a.toString(16):256>a?a.toString(16):"ff"}function z(a,b,c){c=0>c?c+6:6<c?c-6:c;return u(255*(1>c?a+(b-a)*c:3>c?b:4>c?a+(b-
a)*(4-c):a))}function M(a,b){return[r.w(0,0,b.I(0)),r.v(a,b.A,b.u(.5)),r.w(0,0,b.I(1)),r.v(a,b.A,b.u(1)),r.v(a,b.A,b.u(0))]}function A(a,b,c,d,f){var e=0,n=0;function h(c,d,f,h,g){h=h?parseInt(b.charAt(h),16):0;d=d[parseInt(b.charAt(f),16)%d.length];a.G(p[B[c]]);for(c=0;c<g.length;c++)H.m=new m(e+g[c][0]*k,n+g[c][1]*k,k,h++%4),d(H,k,c);a.H()}function g(b){if(0<=b.indexOf(l))for(var a=0;a<b.length;a++)if(0<=B.indexOf(b[a]))return!0}d=c*(void 0===d?.08:d)|0;c-=2*d;var H=new G(a),k=0|c/4,e=e+(0|d+c/
2-2*k),n=n+(0|d+c/2-2*k),p=M(parseInt(b.substr(-7),16)/268435455,f),B=[];for(c=0;3>c;c++){var l=parseInt(b.charAt(8+c),16)%p.length;if(g([0,4])||g([2,3]))l=1;B.push(l)}h(0,C.J,2,3,[[1,0],[2,0],[2,3],[1,3],[0,1],[3,1],[3,2],[0,2]]);h(1,C.J,4,5,[[0,0],[3,0],[3,3],[0,3]]);h(2,C.N,1,null,[[1,1],[2,1],[2,2],[1,2]]);a.finish()}function g(a){return(10*a+.5|0)/10}function I(){this.h=""}function D(a){this.i={};this.M=a;this.size=a.size}function J(a){this.size=Math.min(Number(a.getAttribute("width"))||100,
Number(a.getAttribute("height"))||100);for(this.L=a;a.firstChild;)a.removeChild(a.firstChild);a.setAttribute("viewBox","0 0 "+this.size+" "+this.size);a.setAttribute("preserveAspectRatio","xMidYMid meet")}function K(a){this.size=a;this.l='<svg xmlns="http://www.w3.org/2000/svg" width="'+a+'" height="'+a+'" viewBox="0 0 '+a+" "+a+'" preserveAspectRatio="xMidYMid meet">'}function N(a){return function(b){for(var a=[],d=0;d<b.length;d++)for(var f=b[d],e=28;0<=e;e-=4)a.push((f>>>e&15).toString(16));return a.join("")}(function(b){for(var a=
1732584193,d=4023233417,f=2562383102,e=271733878,n=3285377520,h=[a,d,f,e,n],g=0;g<b.length;g++){for(var l=b[g],k=16;80>k;k++){var p=l[k-3]^l[k-8]^l[k-14]^l[k-16];l[k]=p<<1|p>>>31}for(k=0;80>k;k++)p=(a<<5|a>>>27)+(20>k?(d&f^~d&e)+1518500249:40>k?(d^f^e)+1859775393:60>k?(d&f^d&e^f&e)+2400959708:(d^f^e)+3395469782)+n+l[k],n=e,e=f,f=d<<30|d>>>2,d=a,a=p|0;h[0]=a=h[0]+a|0;h[1]=d=h[1]+d|0;h[2]=f=h[2]+f|0;h[3]=e=h[3]+e|0;h[4]=n=h[4]+n|0}return h}(function(a){function b(a,b){for(var c=[],d=-1,e=0;e<b;e++)d=
e/4|0,c[d]=(c[d]||0)+(f[a+e]<<8*(3-(e&3)));for(;16>++d;)c[d]=0;return c}var d=encodeURI(a),f=[];a=0;var e,g=[];for(e=0;e<d.length;e++){if("%"==d[e]){var h=parseInt(d.substr(e+1,2),16);e+=2}else h=d.charCodeAt(e);f[a++]=h}f[a++]=128;for(e=0;e+64<=a;e+=64)g.push(b(e,64));d=a-e;e=b(e,d);64<d+8&&(g.push(e),e=b(0,0));e[15]=8*a-8;g.push(e);return g}(a)))}function E(a,b){var c=a.canvas.width,d=a.canvas.height;this.g=a;b?this.size=b:(this.size=Math.min(c,d),a.translate((c-this.size)/2|0,(d-this.size)/2|0));
a.clearRect(0,0,this.size,this.size)}function F(){function a(a,b,e){var d=c[a]instanceof Array?c[a]:[b,e];return function(a){a=d[0]+a*(d[1]-d[0]);return 0>a?0:1<a?1:a}}var b=l.config||t.jdenticon_config||{},c=b.lightness||{},b=b.saturation;return{A:"number"==typeof b?b:.5,u:a("color",.4,.8),I:a("grayscale",.3,.9)}}function w(a){return/^[0-9a-f]{11,}$/i.test(a)&&a}function x(a){return N(null==a?"":""+a)}function y(a,b,c){if("string"===typeof a){if(L){a=document.querySelectorAll(a);for(var d=0;d<a.length;d++)y(a[d],
b,c)}}else if(a&&a.tagName){var d=/svg/i.test(a.tagName),f=/canvas/i.test(a.tagName);if(d||f&&"getContext"in a)if(b=w(b)||b&&x(b)||w(a.getAttribute("data-jdenticon-hash"))||a.hasAttribute("data-jdenticon-value")&&x(a.getAttribute("data-jdenticon-value")))a=d?new D(new J(a)):new E(a.getContext("2d")),A(a,b,a.size,c,F())}}function l(){L&&y("[data-jdenticon-hash],[data-jdenticon-value]")}m.prototype={K:function(a,b,c,d){var f=this.o+this.D,e=this.s+this.D;return 1===this.j?new q(f-b-(d||0),this.s+a):
2===this.j?new q(f-a-(c||0),e-b-(d||0)):3===this.j?new q(this.o+b,e-a-(c||0)):new q(this.o+a,this.s+b)}};m.O=new m(0,0,0,0);G.prototype={a:function(a,b){var c=b?-2:2,d=this.m,f=[],e;for(e=b?a.length-2:0;e<a.length&&0<=e;e+=c)f.push(d.K(a[e],a[e+1]));this.C.a(f)},b:function(a,b,c,d){this.C.b(this.m.K(a,b,c,c),c,d)},c:function(a,b,c,d,f){this.a([a,b,a+c,b,a+c,b+d,a,b+d],f)},f:function(a,b,c,d,f,e){a=[a+c,b,a+c,b+d,a,b+d,a,b];a.splice((f||0)%4*2,2);this.a(a,e)},F:function(a,b,c,d,f){this.a([a+c/2,b,
a+c,b+d/2,a+c/2,b+d,a,b+d/2],f)}};var C={N:[function(a,b){var c=.42*b;a.a([0,0,b,0,b,b-2*c,b-c,b,0,b])},function(a,b){var c=0|.5*b;a.f(b-c,0,c,0|.8*b,2)},function(a,b){var c=0|b/3;a.c(c,c,b-c,b-c)},function(a,b){var c=.1*b,c=1<c?0|c:.5<c?1:c,d=6>b?1:8>b?2:0|.25*b;a.c(d,d,b-c-d,b-c-d)},function(a,b){var c=0|.15*b,d=0|.5*b;a.b(b-d-c,b-d-c,d)},function(a,b){var c=.1*b,d=4*c;a.c(0,0,b,b);a.a([d,d,b-c,d,d+(b-d-c)/2,b-c],!0)},function(a,b){a.a([0,0,b,0,b,.7*b,.4*b,.4*b,.7*b,b,0,b])},function(a,b){a.f(b/
2,b/2,b/2,b/2,3)},function(a,b){a.c(0,0,b,b/2);a.c(0,b/2,b/2,b/2);a.f(b/2,b/2,b/2,b/2,1)},function(a,b){var c=.14*b,c=8>b?c:0|c,d=4>b?1:6>b?2:0|.35*b;a.c(0,0,b,b);a.c(d,d,b-d-c,b-d-c,!0)},function(a,b){var c=.12*b,d=3*c;a.c(0,0,b,b);a.b(d,d,b-c-d,!0)},function(a,b){a.f(b/2,b/2,b/2,b/2,3)},function(a,b){var c=.25*b;a.c(0,0,b,b);a.F(c,c,b-c,b-c,!0)},function(a,b,c){var d=.4*b;c||a.b(d,d,1.2*b)}],J:[function(a,b){a.f(0,0,b,b,0)},function(a,b){a.f(0,b/2,b,b/2,0)},function(a,b){a.F(0,0,b,b)},function(a,
b){var c=b/6;a.b(c,c,b-2*c)}]},r={P:function(a,b,c){return"#"+u(a)+u(b)+u(c)},w:function(a,b,c){if(0==b)return a=u(255*c),"#"+a+a+a;b=.5>=c?c*(b+1):c+b-c*b;c=2*c-b;return"#"+z(c,b,6*a+2)+z(c,b,6*a)+z(c,b,6*a-2)},v:function(a,b,c){var d=[.55,.5,.5,.46,.6,.55,.55][6*a+.5|0];return r.w(a,b,.5>c?c*d*2:d+(c-.5)*(1-d)*2)}};I.prototype={a:function(a){for(var b="M"+g(a[0].x)+" "+g(a[0].y),c=1;c<a.length;c++)b+="L"+g(a[c].x)+" "+g(a[c].y);this.h+=b+"Z"},b:function(a,b,c){c=c?0:1;var d=g(b/2),f=g(b);this.h+=
"M"+g(a.x)+" "+g(a.y+b/2)+"a"+d+","+d+" 0 1,"+c+" "+f+",0a"+d+","+d+" 0 1,"+c+" "+-f+",0"}};D.prototype={G:function(a){this.B=this.i[a]||(this.i[a]=new I)},H:function(){},a:function(a){this.B.a(a)},b:function(a,b,c){this.B.b(a,b,c)},finish:function(){for(var a in this.i)this.M.append(a,this.i[a].h)}};J.prototype={append:function(a,b){var c=document.createElementNS("http://www.w3.org/2000/svg","path");c.setAttribute("fill",a);c.setAttribute("d",b);this.L.appendChild(c)}};K.prototype={append:function(a,
b){this.l+='<path fill="'+a+'" d="'+b+'"/>'},toString:function(){return this.l+"</svg>"}};E.prototype={G:function(a){this.g.fillStyle=a;this.g.beginPath()},H:function(){this.g.fill()},a:function(a){var b=this.g,c;b.moveTo(a[0].x,a[0].y);for(c=1;c<a.length;c++)b.lineTo(a[c].x,a[c].y);b.closePath()},b:function(a,b,c){var d=this.g;b/=2;d.arc(a.x+b,a.y+b,b,0,2*Math.PI,c);d.closePath()},finish:function(){}};var L="undefined"!==typeof document&&"querySelectorAll"in document;l.drawIcon=function(a,b,c){if(!a)throw Error("No canvas specified.");
a=new E(a,c);A(a,w(b)||x(b),c,0,F())};l.toSvg=function(a,b,c){var d=new K(b);A(new D(d),w(a)||x(a),b,c,F());return d.toString()};l.update=y;l.version="1.7.2";v&&(v.fn.jdenticon=function(a,b){this.each(function(c,d){y(d,a,b)});return this});"function"===typeof setTimeout&&setTimeout(l,0);return l});