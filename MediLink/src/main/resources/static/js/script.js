document.addEventListener('DOMContentLoaded', () => {
    const fadeElements = document.querySelectorAll('.fade-in');

    const observer = new IntersectionObserver((entries, observer) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.classList.add('visible');  // Add visible class when the element is in view
            }
        });
    }, { threshold: 0.1 });

    fadeElements.forEach(element => {
        observer.observe(element);
    });
});
document.getElementById('scroll-down-btn').addEventListener('click', () => {
    const featuresSection = document.querySelector('.features');
    featuresSection.scrollIntoView({ behavior: 'smooth' });
});
document.addEventListener('DOMContentLoaded', () => {
    const scrollTopBtn = document.getElementById('scroll-top-btn');
    
    // Show the button when user scrolls past the hero section
    window.addEventListener('scroll', () => {
        const heroSection = document.querySelector('.hero');
        const heroBottom = heroSection.getBoundingClientRect().bottom;
        
        if (heroBottom < 0) {  // If the hero section is out of view
            scrollTopBtn.style.display = 'block';  // Show the scroll top button
        } else {
            scrollTopBtn.style.display = 'none';  // Hide the button if the hero section is in view
        }
    });

    // Smooth scroll back to the top when the button is clicked
    scrollTopBtn.addEventListener('click', () => {
        window.scrollTo({
            top: 0,
            behavior: 'smooth'
        });
    });
});
