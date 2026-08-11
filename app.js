const SNAG_DATA = [
  {
    id: 'drawing-room',
    title: 'Drawing Room / Living Area',
    icon: '🏠',
    items: [
      { sno: 1, description: 'Tile hollowness identified in vitrified flooring near the entrance zone' },
      { sno: 2, description: 'Tile undulations observed along the south wall-floor junction' },
      { sno: 3, description: 'Grouting gaps identified between floor tiles near balcony door' },
      { sno: 4, description: 'Wall undulations observed on east wall surface' },
      { sno: 5, description: 'Edge damage / chipping observed on wall corners near passage entry' },
      { sno: 6, description: 'Second coat of putty & paint pending on ceiling surface' },
      { sno: 7, description: 'Paint shade variation observed between adjacent wall surfaces' },
      { sno: 8, description: 'Gaps identified between flooring skirting and wall surface' },
      { sno: 9, description: 'Skirting alignment issue observed near window wall' },
      { sno: 10, description: 'Switchboard face plates missing — all switch points' }
    ]
  },
  {
    id: 'family-area',
    title: 'Family Area / Passage',
    icon: '🚪',
    items: [
      { sno: 1, description: 'Tile hollowness identified near bedroom entry zones' },
      { sno: 2, description: 'Tile offset observed at tile joints along the passage' },
      { sno: 3, description: 'Wall undulations observed on passage wall surface' },
      { sno: 4, description: 'Edge chipping identified at wall corners — multiple locations' },
      { sno: 5, description: 'Second coat of paint pending on walls and ceiling' },
      { sno: 6, description: 'Switchboard face plates missing — all switch points' }
    ]
  },
  {
    id: 'kitchen',
    title: 'Kitchen',
    icon: '🍳',
    items: [
      { sno: 1, description: 'Tile hollowness identified in kitchen flooring area' },
      { sno: 2, description: 'Tile chip-off / damaged tile observed near counter zone' },
      { sno: 3, description: 'Wall tile undulations observed above countertop area' },
      { sno: 4, description: 'Wall tile grouting gaps observed — multiple locations' },
      { sno: 5, description: 'Second coat of paint pending on ceiling surface' },
      { sno: 6, description: 'Gaps observed between floor skirting and wall surface' },
      { sno: 7, description: 'Plumbing — pipe sleeve penetrations unpacked under sink area' },
      { sno: 8, description: 'Switchboard face plates missing — all switch points' }
    ]
  },
  {
    id: 'utility',
    title: 'Utility Area',
    icon: '🧹',
    items: [
      { sno: 1, description: 'Tile hollowness identified in utility flooring' },
      { sno: 2, description: 'Wall undulations observed on wall surfaces' },
      { sno: 3, description: 'Second coat of paint pending on walls and ceiling' },
      { sno: 4, description: 'Unpacked pipe sleeve penetrations observed' },
      { sno: 5, description: 'Gaps observed around sanitary drain fittings' },
      { sno: 6, description: 'Switchboard face plates missing — all switch points' }
    ]
  },
  {
    id: 'master-bedroom',
    title: 'Master Bedroom',
    icon: '🛏️',
    items: [
      { sno: 1, description: 'Tile hollowness identified in bedroom flooring' },
      { sno: 2, description: 'Tile undulations observed along north wall edge' },
      { sno: 3, description: 'Tile offset at joints near wardrobe wall area' },
      { sno: 4, description: 'Wall undulations observed on east wall surface' },
      { sno: 5, description: 'Edge damage / chipping observed on wall corners' },
      { sno: 6, description: 'Second coat of putty & paint pending on ceiling and walls' },
      { sno: 7, description: 'Paint shade variation observed between wall surfaces' },
      { sno: 8, description: 'Gaps identified between flooring skirting and wall surface' },
      { sno: 9, description: 'Door frame damage / dent marks observed on frame edge' },
      { sno: 10, description: 'Door shutter not properly aligned with frame — visible gap' },
      { sno: 11, description: 'Missing fastener caps on door hinges' },
      { sno: 12, description: 'Door hinge screws loosely fixed — tightening required' },
      { sno: 13, description: 'Switchboard face plates missing — all switch points' }
    ]
  },
  {
    id: 'master-toilet',
    title: 'Master Bedroom Toilet',
    icon: '🚿',
    items: [
      { sno: 1, description: 'Tile hollowness identified on wall tiles — multiple locations' },
      { sno: 2, description: 'Tile hollowness identified on floor tiles' },
      { sno: 3, description: 'Grouting gaps observed between wall tiles' },
      { sno: 4, description: 'Gaps observed around wash basin fitting and wall junction' },
      { sno: 5, description: 'Gaps observed around water closet base and floor junction' },
      { sno: 6, description: 'Unpacked pipe sleeve penetrations — behind basin & WC area' },
      { sno: 7, description: 'Sealant missing around shower area floor junction' },
      { sno: 8, description: 'Switchboard face plates missing' }
    ]
  },
  {
    id: 'bedroom-1',
    title: 'Bedroom 1',
    icon: '🛌',
    items: [
      { sno: 1, description: 'Tile hollowness identified in bedroom flooring' },
      { sno: 2, description: 'Tile undulations observed near window wall' },
      { sno: 3, description: 'Wall undulations observed on south wall surface' },
      { sno: 4, description: 'Edge damage / chipping identified at wall corners' },
      { sno: 5, description: 'Second coat of paint pending on ceiling surface' },
      { sno: 6, description: 'Gaps identified between flooring skirting and wall' },
      { sno: 7, description: 'Door frame damage / scratch marks observed' },
      { sno: 8, description: 'Door shutter alignment issue — uneven gap with frame' },
      { sno: 9, description: 'Missing fastener caps on door hardware fittings' },
      { sno: 10, description: 'Switchboard face plates missing — all switch points' }
    ]
  },
  {
    id: 'bedroom-1-toilet',
    title: 'Bedroom 1 Toilet',
    icon: '🚿',
    items: [
      { sno: 1, description: 'Tile hollowness identified on floor tiles' },
      { sno: 2, description: 'Wall tile grouting gaps observed — multiple locations' },
      { sno: 3, description: 'Gaps observed around wash basin fitting and wall junction' },
      { sno: 4, description: 'Unpacked pipe sleeve penetrations — behind basin area' },
      { sno: 5, description: 'Switchboard face plates missing' }
    ]
  },
  {
    id: 'bedroom-2',
    title: 'Bedroom 2',
    icon: '🛌',
    items: [
      { sno: 1, description: 'Tile hollowness identified in bedroom flooring' },
      { sno: 2, description: 'Tile chip-off / damaged tile observed near door entry' },
      { sno: 3, description: 'Wall undulations observed on west wall surface' },
      { sno: 4, description: 'Second coat of paint pending on walls and ceiling' },
      { sno: 5, description: 'Door frame damage / dent marks observed' },
      { sno: 6, description: 'Missing fastener caps on door hardware fittings' },
      { sno: 7, description: 'Switchboard face plates missing — all switch points' }
    ]
  },
  {
    id: 'bedroom-2-toilet',
    title: 'Bedroom 2 Toilet',
    icon: '🚿',
    items: [
      { sno: 1, description: 'Tile hollowness identified on floor tiles' },
      { sno: 2, description: 'Wall tile offset observed at tile joints' },
      { sno: 3, description: 'Gaps observed around wash basin sanitary fitting' },
      { sno: 4, description: 'Gaps observed around water closet base and floor' },
      { sno: 5, description: 'Unpacked pipe sleeve penetrations — behind WC area' },
      { sno: 6, description: 'Switchboard face plates missing' }
    ]
  },
  {
    id: 'bedroom-3',
    title: 'Bedroom 3 / Home Office',
    icon: '💼',
    items: [
      { sno: 1, description: 'Tile hollowness identified in room flooring' },
      { sno: 2, description: 'Wall undulations observed on wall surfaces' },
      { sno: 3, description: 'Edge damage identified at wall corners' },
      { sno: 4, description: 'Second coat of paint pending on walls and ceiling' },
      { sno: 5, description: 'Door frame damage / poor polish finish observed' },
      { sno: 6, description: 'Switchboard face plates missing — all switch points' },
      { sno: 7, description: 'External CP fittings pending — adjacent Bedroom 4 area' }
    ]
  },
  {
    id: 'balcony',
    title: 'Balcony',
    icon: '🌿',
    items: [
      { sno: 1, description: 'Cracked granite upstand slab on parapet wall — replacement required' },
      { sno: 2, description: 'Damaged window mesh on balcony enclosure panel' },
      { sno: 3, description: 'Glass scratches observed on balcony window panels' },
      { sno: 4, description: 'Damping / moisture shades visible on corner wall surfaces' },
      { sno: 5, description: 'Sealant gaps observed at window frame and wall junction' },
      { sno: 6, description: 'Floor tile grouting gaps observed — multiple locations' }
    ]
  },
  {
    id: 'windows-upvc',
    title: 'Windows & UPVC Works',
    icon: '🪟',
    items: [
      { sno: 1, description: 'Mortar residue on glass panels — Drawing Room window' },
      { sno: 2, description: 'Mortar residue on glass panels — Master Bedroom window' },
      { sno: 3, description: 'Construction stains on UPVC frames — multiple window locations' },
      { sno: 4, description: 'Glass scratches observed — Bedroom 1 window panel' },
      { sno: 5, description: 'Glass scratches observed — Bedroom 3 window panel' },
      { sno: 6, description: 'Window tracks uncleaned with dust and debris — all windows' },
      { sno: 7, description: 'Sealant gaps between UPVC frame and wall — multiple locations' },
      { sno: 8, description: 'Missing fastener caps on window hardware fittings' },
      { sno: 9, description: 'Sharp / protruding fastener screws — Bedroom 2 window' },
      { sno: 10, description: 'Damaged window mesh panel — Bedroom 3 window' },
      { sno: 11, description: 'Damaged rubber gaskets on sliding window panels' }
    ]
  },
  {
    id: 'doors',
    title: 'Main Door & Internal Doors',
    icon: '🚪',
    items: [
      { sno: 1, description: 'Main door — eye piece / door viewer (peephole) missing' },
      { sno: 2, description: 'Main door — frame edge damage near hinge side' },
      { sno: 3, description: 'Main door — lock alignment issue causing stiff operation' },
      { sno: 4, description: 'Internal door (Master BR) — frame damage with visible dent marks' },
      { sno: 5, description: 'Internal door (Master BR) — poor polish / uneven paint finish' },
      { sno: 6, description: 'Internal door (Bedroom 1) — shutter-frame alignment gap visible' },
      { sno: 7, description: 'Internal door (Bedroom 2) — frame scratch / surface damage' },
      { sno: 8, description: 'Internal door (Home Office) — poor polish finish on frame surface' },
      { sno: 9, description: 'Missing fastener caps on all internal door hinges & hardware fittings' }
    ]
  },
  {
    id: 'electrical',
    title: 'Electrical Works',
    icon: '⚡',
    items: [
      { sno: 1, description: 'Switchboard face plates missing — Drawing Room switch points' },
      { sno: 2, description: 'Switchboard face plates missing — Kitchen switch points' },
      { sno: 3, description: 'Switchboard face plates missing — Master Bedroom switch points' },
      { sno: 4, description: 'Switchboard face plates missing — Bedroom 1 switch points' },
      { sno: 5, description: 'Switchboard face plates missing — Bedroom 2 switch points' },
      { sno: 6, description: 'Switchboard face plates missing — Home Office switch points' },
      { sno: 7, description: 'Switchboard face plates missing — All Toilet switch points' },
      { sno: 8, description: 'Switchboard face plates missing — Utility area switch points' },
      { sno: 9, description: 'Switchboard face plates missing — Passage / Family Area switch points' },
      { sno: 10, description: 'Gaps observed around Distribution Board (DB) panel edges' },
      { sno: 11, description: 'DB cover drawing / circuit labeling documentation pending' }
    ]
  },
  {
    id: 'plumbing',
    title: 'Plumbing & Sanitary Works',
    icon: '🔧',
    items: [
      { sno: 1, description: 'Unpacked pipe sleeve penetrations — Master Bedroom Toilet' },
      { sno: 2, description: 'Unpacked pipe sleeve penetrations — Bedroom 1 Toilet' },
      { sno: 3, description: 'Unpacked pipe sleeve penetrations — Bedroom 2 Toilet' },
      { sno: 4, description: 'Unpacked pipe sleeve penetrations — Kitchen (under sink)' },
      { sno: 5, description: 'Unpacked pipe sleeve penetrations — Utility Area' },
      { sno: 6, description: 'Gaps around wash basin fitting — Master Bedroom Toilet' },
      { sno: 7, description: 'Gaps around wash basin fitting — Bedroom 1 Toilet' },
      { sno: 8, description: 'Gaps around wash basin fitting — Bedroom 2 Toilet' },
      { sno: 9, description: 'Gaps around water closet base — Master Bedroom Toilet' },
      { sno: 10, description: 'Gaps around water closet base — Bedroom 2 Toilet' },
      { sno: 11, description: 'External CP (chrome plated) fittings pending — Bedroom 4 area' }
    ]
  }
];

const DIMENSION_DATA = [
  { area: 'Bedroom 1', brochure: '10 ft × 10 ft', measured: '10 ft × 10 ft', status: 'ok', comment: 'Satisfactory' },
  { area: 'Bedroom 1 Toilet', brochure: '4.6 ft × 8.3 ft', measured: '4.3 ft × 8 ft', status: 'warn', comment: 'Discrepancy — Smaller than brochure' },
  { area: 'Master Bedroom', brochure: '10 ft × 12.6 ft', measured: '10 ft × 12.5 ft', status: 'ok', comment: 'Satisfactory' },
  { area: 'Master Toilet', brochure: '8.3 ft × 4.6 ft', measured: '8 ft × 4.4 ft', status: 'warn', comment: 'Discrepancy — Smaller than brochure' },
  { area: 'Bedroom 2', brochure: '11.6 ft × 10 ft', measured: '11.5 ft × 10 ft', status: 'ok', comment: 'Satisfactory' },
  { area: 'Bedroom 2 Toilet', brochure: '6.9 ft × 5 ft', measured: '6.6 ft × 4.7 ft', status: 'warn', comment: 'Discrepancy — Smaller than brochure' },
  { area: 'Home Office', brochure: '10.2 ft × 10 ft', measured: '10 ft × 10 ft', status: 'ok', comment: 'Satisfactory' },
  { area: 'Kitchen', brochure: '7.9 ft × 12 ft', measured: '7.7 ft × 11.10 ft', status: 'ok', comment: 'Satisfactory' },
  { area: 'Utility', brochure: '4 ft × 7.9 ft', measured: '4 ft × 6.4 ft', status: 'warn', comment: 'Discrepancy — Smaller than brochure' },
  { area: 'Drawing Room', brochure: '14.3 ft × 13 ft', measured: '14.2 ft × 13 ft', status: 'ok', comment: 'Satisfactory' },
  { area: 'Family Area', brochure: '10 ft × 30.1 ft', measured: '10 ft × 27.8 ft', status: 'ok', comment: 'Satisfactory' },
  { area: 'Balcony', brochure: '10 ft × 6 ft', measured: '10 ft × 6.4 ft', status: 'ok', comment: 'Satisfactory' }
];

const uploadedImages = new Map();
let totalItems = 0;

document.addEventListener('DOMContentLoaded', () => {
    // Calculate total snag items for progress
    totalItems = SNAG_DATA.reduce((sum, section) => sum + section.items.length, 0);

    renderSnagSections();
    renderDimensionTable();
    updateProgress();

    // View Navigation Listeners
    const btnToSnaglist = document.getElementById('view-snaglist-btn');
    if (btnToSnaglist) {
        btnToSnaglist.addEventListener('click', () => showView('snaglist-view'));
    }

    const btnToDashboard = document.getElementById('back-to-dashboard-btn');
    if (btnToDashboard) {
        btnToDashboard.addEventListener('click', () => showView('dashboard-view'));
    }

    // Modal Close Listeners
    const modal = document.getElementById('image-modal');
    const closeBtn = document.getElementById('modal-close');
    
    if (closeBtn) {
        closeBtn.addEventListener('click', closeModal);
    }
    
    if (modal) {
        modal.addEventListener('click', (e) => {
            if (e.target === modal) {
                closeModal();
            }
        });
    }
});

function showView(viewId) {
    const views = document.querySelectorAll('.view');
    views.forEach(view => {
        view.classList.remove('active');
        view.style.display = 'none'; 
    });

    const targetView = document.getElementById(viewId);
    if (targetView) {
        targetView.style.display = 'block';
        targetView.classList.add('active');
        window.scrollTo(0, 0);
    }
}

function renderSnagSections() {
    const container = document.getElementById('snag-sections-container');
    if (!container) return;

    container.innerHTML = '';

    SNAG_DATA.forEach(section => {
        const sectionEl = document.createElement('div');
        sectionEl.className = 'snag-section';

        const header = document.createElement('div');
        header.className = 'section-header';
        header.innerHTML = `
            <div class="section-title">
                <span class="section-icon">${section.icon}</span>
                <h3>${section.title}</h3>
            </div>
            <span class="toggle-icon">▼</span>
        `;

        const body = document.createElement('div');
        body.className = 'section-body';

        const table = document.createElement('table');
        table.className = 'snag-table';
        table.innerHTML = `
            <thead>
                <tr>
                    <th>S.No</th>
                    <th>Issue Description</th>
                    <th>Proof</th>
                </tr>
            </thead>
            <tbody>
                ${section.items.map(item => `
                    <tr>
                        <td>${item.sno}</td>
                        <td>${item.description}</td>
                        <td>
                            <div class="upload-container" data-id="${section.id}-${item.sno}">
                                <label class="upload-btn">
                                    <span class="btn-text">📷 Upload Photo</span>
                                    <input type="file" accept="image/*" capture="environment" style="display: none;">
                                </label>
                                <div class="preview-container" style="display: none;">
                                    <img class="thumbnail" src="" alt="Proof">
                                </div>
                            </div>
                        </td>
                    </tr>
                `).join('')}
            </tbody>
        `;

        body.appendChild(table);
        sectionEl.appendChild(header);
        sectionEl.appendChild(body);
        container.appendChild(sectionEl);

        // Section Toggle Logic
        header.addEventListener('click', () => {
            const isActive = header.classList.contains('active');
            header.classList.toggle('active', !isActive);
            body.classList.toggle('open', !isActive);
            body.style.display = isActive ? 'none' : 'block';
        });

        // Initialize state
        body.style.display = 'none';
    });

    // Event Delegation for File Uploads
    container.addEventListener('change', (e) => {
        if (e.target.type === 'file') {
            const input = e.target;
            const uploadContainer = input.closest('.upload-container');
            if (!uploadContainer) return;

            const itemId = uploadContainer.dataset.id;
            const btnText = uploadContainer.querySelector('.btn-text');
            const previewContainer = uploadContainer.querySelector('.preview-container');
            const thumbnail = uploadContainer.querySelector('.thumbnail');

            if (input.files && input.files[0]) {
                const file = input.files[0];
                const reader = new FileReader();

                reader.onload = (event) => {
                    const dataUrl = event.target.result;
                    uploadedImages.set(itemId, dataUrl);

                    thumbnail.src = dataUrl;
                    previewContainer.style.display = 'block';
                    btnText.textContent = '✅ Uploaded';
                    
                    const label = input.closest('label');
                    if(label) label.classList.add('uploaded');

                    updateProgress();
                };

                reader.readAsDataURL(file);
            }
        }
    });

    // Event Delegation for Image Modal
    container.addEventListener('click', (e) => {
        if (e.target.classList.contains('thumbnail')) {
            openModal(e.target.src);
        }
    });
}

function renderDimensionTable() {
    let dimensionSectionBody = document.querySelector('#dimension-section .section-body');
    
    // If the dimension section isn't pre-defined in HTML, append it as the LAST section
    if (!dimensionSectionBody) {
        const container = document.getElementById('snag-sections-container');
        if (!container) return;

        const sectionEl = document.createElement('div');
        sectionEl.id = 'dimension-section';
        sectionEl.className = 'snag-section';

        const header = document.createElement('div');
        header.className = 'section-header';
        header.innerHTML = `
            <div class="section-title">
                <span class="section-icon">📏</span>
                <h3>Room Dimensions</h3>
            </div>
            <span class="toggle-icon">▼</span>
        `;

        const body = document.createElement('div');
        body.className = 'section-body';

        sectionEl.appendChild(header);
        sectionEl.appendChild(body);
        container.appendChild(sectionEl);

        header.addEventListener('click', () => {
            const isActive = header.classList.contains('active');
            header.classList.toggle('active', !isActive);
            body.classList.toggle('open', !isActive);
            body.style.display = isActive ? 'none' : 'block';
        });

        body.style.display = 'none';
        dimensionSectionBody = body;
    } else {
        dimensionSectionBody.innerHTML = '';
    }

    const table = document.createElement('table');
    table.className = 'snag-table dimension-table';
    table.innerHTML = `
        <thead>
            <tr>
                <th>S.No</th>
                <th>Area</th>
                <th>Brochure Dimension</th>
                <th>Measured In-Site</th>
                <th>Status</th>
            </tr>
        </thead>
        <tbody>
            ${DIMENSION_DATA.map((item, index) => {
                const isOk = item.status === 'ok';
                const statusColor = isOk ? 'green' : 'red';
                const statusText = isOk ? '✅ Satisfactory' : '⚠️ Discrepancy';
                return `
                    <tr>
                        <td>${index + 1}</td>
                        <td>${item.area}</td>
                        <td>${item.brochure}</td>
                        <td>${item.measured}</td>
                        <td style="color: ${statusColor};">
                            ${statusText}
                            ${item.comment && !isOk ? `<br><small style="color: #666;">${item.comment}</small>` : ''}
                        </td>
                    </tr>
                `;
            }).join('')}
        </tbody>
    `;

    dimensionSectionBody.appendChild(table);
}

function updateProgress() {
    const progressFill = document.getElementById('progress-fill');
    const progressText = document.getElementById('progress-text');
    
    if (!progressFill || !progressText) return;

    const uploadedCount = uploadedImages.size;
    let percentage = 0;
    
    if (totalItems > 0) {
        percentage = (uploadedCount / totalItems) * 100;
    }

    progressFill.style.width = `${percentage}%`;
    progressText.textContent = `${uploadedCount} of ${totalItems} issues documented`;
}

function openModal(imageSrc) {
    const modal = document.getElementById('image-modal');
    const modalImg = document.getElementById('modal-image') || modal?.querySelector('img');
    
    if (modal && modalImg) {
        modalImg.src = imageSrc;
        modal.classList.add('active');
        modal.style.display = 'flex'; // Assuming flex for centering
    }
}

function closeModal() {
    const modal = document.getElementById('image-modal');
    if (modal) {
        modal.classList.remove('active');
        modal.style.display = 'none';
    }
}
