/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */
<<<<<<< HEAD
/*$(window).scroll(function(){
    if($(window).scrollTop()>60){
        document.getElementById("banner").style="background: rgb(0,0,0); background: linear-gradient(0deg, rgba(0,0,0,0.6110819327731092) 50%, rgba(0,0,0,1) 100%); box-shadow: none;";
    }else{
        document.getElementById("banner").style="background: rgb(0,0,0); box-shadow: 0px 20px 30px 0px;";
    }
});*/
var winWith = $(window).width();
var winHeight = $(window).height();

$(".error").ready(function(){
    $(".error").css("width",winWith);
    $(".error").css("height",winHeight);
});

$(window).resize(function(){
    console.log("hola");
    $(".error").width(winWith);
    $(".error").height(winHeight);
});

if (window.history.replaceState) {
    window.history.replaceState(null,null,window.location.href);
=======
const menuBtn=document.getElementsByClassName("menu");
function verMenu(){
    console.log(menuBtn);
        if (document.getElementById("menu_web").getAttribute("style")==="display: block;") {
            document.getElementById("menu_web").style="display:none;";
            console.log("click_menu"+document.getElementById("menu_web").getAttribute("display"));
        }else{
            document.getElementById("menu_web").style="display:block;";
            console.log("click_menu_visible : "+document.getElementById("menu_web").getAttribute("style"));
        };
};

function verMenuUser(){
    console.log(menuBtn);
        if (document.getElementById("menu_web_user").getAttribute("style")==="display: block;") {
            document.getElementById("menu_web_user").style="display:none;";
            console.log("click_menu"+document.getElementById("menu_web_user").getAttribute("display"));
        }else{
            document.getElementById("menu_web_user").style="display:block;";
            console.log("click_menu_visible : "+document.getElementById("menu_web_user").getAttribute("style"));
        };
};

let script=document.createElement('script');

function ver(){
    console.log(document.getElementById("about_Background_Resource").play());
}

if (window.performance) {
  console.info("window.performance works fine on this browser");
}
console.info(performance.navigation.type);
if (performance.navigation.type === performance.navigation.TYPE_RELOAD) {
    document.getElementById("body").click();
    document.getElementById("about_Background_Resource").autofocus="true";
    document.getElementById("about_Background_Resource").play();
    //document.getElementById("about_Background_Resource").muted="muted";
    console.log(document.getElementById("about_Background_Resource").autofocus);
  console.info( "This page is reloaded" );
} else {
  console.info( "This page is not reloaded");
>>>>>>> 3ec3023e315bb2dc3952b0d72da17fb39fbf9c91
}