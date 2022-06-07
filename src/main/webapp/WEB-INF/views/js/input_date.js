let input = document.querySelector('input.date');
input.addEventListener('input', function() {
    this.previousElementSibling.textContent = this.value.padEnd(6, 'X');
});