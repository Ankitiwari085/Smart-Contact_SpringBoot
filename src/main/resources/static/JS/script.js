document.addEventListener("DOMContentLoaded", () => {
  const toggleBtn = document.querySelector(".sidebar-toggle");
  const sidebar = document.querySelector(".sidebar");
  const contentWrapper = document.querySelector(".content-wrapper");



  toggleBtn.addEventListener("click", () => {
    sidebar.classList.toggle("active");
    contentWrapper.classList.toggle("shifted");
  });

  // Auto-close sidebar when link clicked (for mobile UX)
  document.querySelectorAll(".sidebar a").forEach(link => {
    link.addEventListener("click", () => {
      if (window.innerWidth <= 768) {
        sidebar.classList.remove("active");
        contentWrapper.classList.remove("shifted");
      }
    });
  });
});

//
//const search=()=>{
//let query=$("#search-input").val();
//if(query==''){
//$(".search-result").hide();
//}else{
//let url=`http://localhost:8080/search/${query}`;
//fetch(url).then(response=>{
//return response.json}).then((data)=>{
//let text=`<div class="list-group">`
//data.forEach((contact)=>{
//text+=`<a href='/user/${contact.cId}/contact' class="list-group-item list-group-action">${contact.name}</a>`;
//});
//text+=`</div>`;
//$(".search-result").html(text);
//$(".search-result").show();
//});
//
//}
//}


window.search = () => {
    let query = $("#search-input").val().trim();

    if (query === '') {
        $(".search-result").hide();
    } else {
        let url = `http://localhost:8080/search/${encodeURIComponent(query)}`;

        fetch(url)
            .then(response => response.json())
            .then(data => {
                let text = `<div class="list-group">`;

                data.forEach(contact => {
                    text += `
                        <a href="/user/${contact.cId}/contact"
                           class="list-group-item list-group-item-action">
                           ${contact.name}
                        </a>`;
                });

                text += `</div>`;
                $(".search-result").html(text).show();
            })
            .catch(error => {
                console.error("Error fetching search results:", error);
                $(".search-result").hide();
            });
    }
};
