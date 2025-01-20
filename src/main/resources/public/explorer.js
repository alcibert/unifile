let app = document.getElementById("explorer");
let markedDirectory = "";

function fetchData(cwd = "."){
    url = `/api/v1.0/directory/explore?cwd=${cwd}`;
    fetch(url)
        .then(response => response.json())
        .then((data) => {
            handleResponse(data);
        });
}
fetchData();

function handleResponse(payload){
    app.replaceChildren();
    createHeader(payload);
    createNavigation(payload);
    createFolderContent(payload);
    createFooter(payload);
}

function createHeader(response){
    let headerElement = document.createElement("div");
    headerElement.classList.add("titlebar");

    let img = document.createElement("img");
    img.src = "./icons/favicon.png";
    img.alt = "Fenster Icon";
    img.classList.add("favicon");
    headerElement.appendChild(img);

    let view = document.createElement("span");
    view.classList.add("view");
    view.innerText = "Ansicht";
    headerElement.appendChild(view);

    let title = document.createElement("span");
    title.classList.add("title");
    title.innerText = response.absolutePath.split("\/")[response.absolutePath.split("\/").length -1];
    headerElement.appendChild(title);

    let close = document.createElement("span");
    close.classList.add("close");
    close.innerHTML = "&#9587;";
    close.addEventListener('click', function(e){
        app.style.display = 'none';
    });
    headerElement.appendChild(close);
    app.appendChild(headerElement);
}
function createNavigation(payload){

    let nav = document.createElement("div");
    nav.classList.add("navBar");

    let back = document.createElement("span");
    back.classList.add("back","cursorNotAllowed");
    back.setAttribute('title', "reserve for future use");
    back.innerHTML = "&larr;";
    nav.appendChild(back);

    let up = document.createElement("span");
    up.classList.add("up","cursorNotAllowed");
    up.setAttribute('title', "reserve for future use");
    up.innerHTML = "&uarr;";
    nav.appendChild(up);

    let pathBar = document.createElement("div");
    pathBar.classList.add("pathBar");
    let img = document.createElement("img");
    img.src = "./icons/favicon.png";
    img.alt = "Fenster Icon";
    img.classList.add("favicon");
    pathBar.appendChild(img);

    let pathArray = payload.absolutePath.split("\\");
    let tempPath;
    let scanPath = "";
    for (const pathObject of pathArray) {
        scanPath = `${scanPath}${pathObject}/`;
        tempPath = document.createElement("span");
        tempPath.classList.add("pathObj");
        tempPath.innerText = pathObject;
        tempPath.addEventListener("click", fetchData.bind(this, encodeURIComponent(scanPath)), false);
        pathBar.appendChild(tempPath);
    }
    nav.appendChild(pathBar);

    let searchBar = document.createElement("div");
    searchBar.classList.add("searchBar");

    let icon = document.createElement("span");
    icon.classList.add("searchIcon");
    icon.innerText = "🔎";
    searchBar.appendChild(icon);

    let input = document.createElement("input");
    input.type = "text";
    input.id = "searchInput";
    input.placeholder = `"${payload.absolutePath.split("\/")[payload.absolutePath.split("\/").length -1]}" durchsuchen`;
    searchBar.appendChild(input);

    nav.appendChild(searchBar);
    app.appendChild(nav);
}

function createFolderContent(payload){
    let contentScroll = document.createElement("div");
    contentScroll.classList.add("contentScroll");
    let folderContent = document.createElement("div");
    folderContent.classList.add("folderContent");

    let header = document.createElement("div");
    header.classList.add("header");

    let Hname = document.createElement("span");
    Hname.innerText = "Name";
    header.appendChild(Hname);
    let Hmodified = document.createElement("span");
    Hmodified.innerText = "Änderungsdatum";
    header.appendChild(Hmodified);
    let Htype = document.createElement("span");
    Htype.innerText = "Typ";
    header.appendChild(Htype);
    let Hsize = document.createElement("span");
    Hsize.innerText = "Größe";
    header.appendChild(Hsize);

    folderContent.appendChild(header);

    for(element of payload.content){
        createFile(element, folderContent);
    }
    
    contentScroll.appendChild(folderContent);
    app.appendChild(contentScroll);
}

function createFile(file, toAppend){
    let fileElement = document.createElement("div");
    fileElement.classList.add("contentRow");
    let fileTitle = document.createElement("span");
    fileTitle.classList.add("name");
    fileTitle.innerText = file.name;
    fileElement.appendChild(fileTitle);

    let fileModified = document.createElement("span");
    fileModified.classList.add("modified");
    // let date = new Date(Date.parse(file.lastModified))
    let date = new Date(file.lastModified)
    fileModified.innerText = date.toLocaleString();
    fileElement.appendChild(fileModified);

    let fileFormat = document.createElement("span");
    fileFormat.classList.add("type");
    // fileFormat.innerText = file.format
    // ToDo: File Extension hier einfügen. Wenn nicht vorhanden, dann ist es ein directory
    fileFormat.innerText = file.isDirectory ? "directory" : "file";
    fileElement.appendChild(fileFormat);


    let filesize = document.createElement("span");
    filesize.classList.add("size");
    // ToDo: size umrechnen zur nächst höheren Kilo Stufe
    filesize.innerText = `${file.size} B`;
    fileElement.appendChild(filesize);
    let frontOfPath = "";
    if(file.absolutePath != ""){
        frontOfPath = `${file.absolutePath}\\`
    }
    let eventListenerUrl = `${frontOfPath}${file.name}`.replaceAll("\\", "/");
    if(file.isDirectory){
        fileElement.addEventListener("dblclick", fetchData.bind(this, encodeURIComponent(eventListenerUrl)), false);
        console.log(encodeURI(eventListenerUrl))
        fileElement.addEventListener("click", function(e){
            markedDirectory = eventListenerUrl;
            document.getElementById("path").innerHTML=markedDirectory;
        });
    }
    toAppend.appendChild(fileElement);
}

function createFooter(payload){
    let footer = document.createElement("div");
    footer.classList.add("footer")
    let path = document.createElement("span");
    path.setAttribute("id", "path");
    let selectButton = document.createElement("button");
    selectButton.innerHTML="Ordner auswählen";
    // selectButton.classList.add("");
    selectButton.addEventListener("click", function(e){
        console.log(markedDirectory);
        // TODO:::: const overlayEvent = new CustomEvent('overlayValue', { detail: { value: inputValue } });
        // document.dispatchEvent(overlayEvent);
        document.getElementById("pathSelectorA").value = markedDirectory;
        document.getElementById("explorer").style.display = "none";
    });
    footer.appendChild(path);
    footer.appendChild(selectButton);
    app.appendChild(footer);


}