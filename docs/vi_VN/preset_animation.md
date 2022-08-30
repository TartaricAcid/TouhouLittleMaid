

# Hoạt ảnh có sẵn

Có rất nhiều hoạt ảnh mặc định sẵn có trong mod, bạn chỉ cần:
1. Đặt đúng tên cho nhóm mà bạn muốn tạo hoạt động trong Blockbench.
2. Gọi tên đường dẫn đến vùng `animation` của tệp `maid_model.json`.

> Plugin `tlm-utils` có thể tự động gọi tên đường dẫn đến hoạt ảnh dựa vào tên nhóm mô hình, bạn chỉ cần nhấp vào nút `Re-Analyze Animation`. ![img](https://i.imgur.com/iyCKwMx.gif)

Plugin này trên Blockbench cung cấp toàn bộ hoạt ảnh có sẵn khả dụng, bạn có thể nhấp chuột phải trong vùng outliner để mở giao diện hoạt ảnh có sẵn   
![img](https://i.imgur.com/N17PbiE.gif)


## Sắp xếp các nhóm
Dành cho việc render, các trục pivot của các nhóm có thể được bỏ trống.
### `armLeftPositioningBone`

Để sắp xếp vị trí cho vật phẩm cầm trên tay trái, nhóm mô hình của nó phải là `armLeft`

### `armRightPositioningBone`

Để sắp xếp vị trí cho vật phẩm cầm trên tay phải, nhóm mô hình của nó phải là `armRight`

### `backpackPositioningBone`

Để sắp xếp vị trí cho ba lô đeo trên lưng, nhóm mô hình của nó phải là root