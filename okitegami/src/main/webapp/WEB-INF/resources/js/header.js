/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */
//Menu
///Show
////Mouse
$("#bt_menu_header").mouseover( function(){
    document.getElementById("menu_web").style="display:block;";
});

$("#bt_menu_header").mouseout( function(){
    document.getElementById("menu_web").style="display:none;";
});
$("#menu_web").mouseover( function(){
    document.getElementById("menu_web").style="display:block;";
});

$("#menu_web").mouseout( function(){
    document.getElementById("menu_web").style="display:none;";
});
////Touch

//Close
////Mouse
$(window).click(function(e){
    if(document.getElementById("bt_menu_header").contains(e.target)){
       document.getElementById("menu_web").style="display:block;"; 
    }else{
        document.getElementById("menu_web").style="display:none;";
    }
    if(document.getElementById("userImageBanner").contains(e.target)){
        document.getElementById("menu_web_user").style="display:block;";
    }if(document.getElementById("optUser").contains(e.target)){
        document.getElementById("menu_web_user").style="display:block;";
    }else{
        document.getElementById("menu_web_user").style="display:none;";
    }
});
////Touch


//User menu
$("#optUser").mouseover( function(){
        document.getElementById("menu_web_user").style="display:block;";
});

$("#optUser").mouseout( function(){
    document.getElementById("menu_web_user").style="display:none;";
});

$("#menu_web_user").mouseover( function(){
    document.getElementById("menu_web_user").style="display:block;";
});

$("#menu_web_user").mouseout( function(){
    document.getElementById("menu_web_user").style="display:none;";
});

