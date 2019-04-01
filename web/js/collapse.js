var triggers = Array.from(document.querySelectorAll('[data-toggle="collapse"]'));
window.addEventListener('click', function (ev) {
    var elm = ev.target;

    if (triggers.includes(elm)) {
        var selector = elm.getAttribute('data-target');
        collapse(selector, 'toggle');
    }
}, false);
var fnmap = {
    'toggle': 'toggle',
    'show': 'add',
    'hide': 'remove'
};

var collapse = function collapse(selector, cmd) {
    var targets = Array.from(document.querySelectorAll(selector));
    targets.forEach(function (target) {
        target.classList[fnmap[cmd]]('show');
    });
};