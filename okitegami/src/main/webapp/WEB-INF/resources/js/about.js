/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

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
}