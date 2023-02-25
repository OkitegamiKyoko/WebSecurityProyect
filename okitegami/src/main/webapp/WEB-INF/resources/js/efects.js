/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */
const menuBtn=document.getElementsByClassName("menu");
menuBtn.addEventListener("mouseover",verMenu);
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

let script=document.createElement('script');
function ver(){
  script  
};