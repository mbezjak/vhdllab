var req;

function performAjaxCall(data) {
  if( window.XMLHttpRequest ) {
    req = new XMLHttpRequest();
  } else if( window.ActiveXObject ) {
    req = new ActiveXObject("Microsoft.XMLHTTP");
  }
  req.open("POST", "doAjax", true);
  req.onreadystatechange = ajaxCallback;
  req.setRequestHeader("Content-Type","text/xml;charset=utf-8");
  req.send(String(data));
}

function performAjaxCallEx(url, data) {
  if( window.XMLHttpRequest ) {
    req = new XMLHttpRequest();
  } else if( window.ActiveXObject ) {
    req = new ActiveXObject("Microsoft.XMLHTTP");
  }
  url = String(url);
  req.open("POST", url, true);
  req.onreadystatechange = ajaxCallback;
  req.setRequestHeader("Content-Type","text/xml;charset=utf-8");
  req.send(String(data));
}

function performSyncAjaxCall(data) {
  if( window.XMLHttpRequest ) {
    req = new XMLHttpRequest();
  } else if( window.ActiveXObject ) {
    req = new ActiveXObject("Microsoft.XMLHTTP");
  }
  req.open("POST", "doAjax", false);
  req.setRequestHeader("Content-Type","text/xml;charset=utf-8");
  req.send(String(data));
  return req.responseText;
}

function performSyncAjaxCallEx(url, data) {
  if( window.XMLHttpRequest ) {
    req = new XMLHttpRequest();
  } else if( window.ActiveXObject ) {
    req = new ActiveXObject("Microsoft.XMLHTTP");
  }
  url = String(url);
  req.open("POST", url, false);
  req.setRequestHeader("Content-Type","text/xml;charset=utf-8");
  req.send(String(data));
    return req.responseText;
}

function performAjaxAbort() {
  req.abort();
}

function ajaxCallback() {
  if( req.readyState==4) {
    document.getElementById("vhdllab").ajaxCallResultReceived(req.responseText, req.status);
  }
}

function exitApplication() {
  document.getElementById("vhdllab").fastCleanup();
}