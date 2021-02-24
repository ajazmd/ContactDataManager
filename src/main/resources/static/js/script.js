console.log("This is script file");

<!-- Sidebar show and Hide function -->

const toggleSidebar=()=>{
if($(".sidebar").is(":visible")){
    $(".sidebar").css("display","none");
    $(".content").css("margin-left","0%");
    }
    else{
    $(".sidebar").css("display","block");
    $(".content").css("margin-left","20%");
    }
 };
 
<!-- Search function to call /search/{query} -->

const search=()=>{
   let query=$("#search-input").val();
   console.log(query);
  if(query==''){
    $(".search-result").hide("slow");
   }
  else{
    console.log(query);
    let url = `http://localhost:8080/search/${query}`;
    fetch(url)
    .then((response)=>{
    return response.json();
    })
    .then((data)=>{
    console.log(data);
     let text = `<div class='list-group'>`;
    data.forEach((contactsall)=>{
    text += `<a href='/user/${contactsall.cId}/contact' class='list-group-item list-group-item-action'>${contactsall.name}</a>`;
    })
    text += `</div>`;
    $(".search-result").html(text);
    $(".search-result").show("fast");
    });
  }
 };



