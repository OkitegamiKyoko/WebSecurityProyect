/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */
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
}