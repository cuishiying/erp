var strUtils;
strUtils={
    splitImg:function (img) {
        var i = img[0].src;
        var s = i.lastIndexOf('/');
        return i.substring(s+1);
    }
}