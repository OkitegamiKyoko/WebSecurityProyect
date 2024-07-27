/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */
let originalSRC=document.getElementById("userImage").src;
const file=document.getElementById('userImageButon');
file.addEventListener('change',e=>{
    
    let preview=document.getElementById("userImage");
    try{
        let reader=new FileReader();
        reader.readAsDataURL(e.target.files[0]);
        reader.onload=function(){
            preview.src=reader.result;
        };
    }catch(error){
            preview.src=originalSRC;
    };
});

