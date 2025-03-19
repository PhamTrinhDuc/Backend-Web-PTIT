//package com.javaweb.api;
//
//import com.javaweb.exception.FieldRequiredException;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//// GET: lấy dữ liệu
//// POST: thêm dữ liệu
//// PUT: sửa dữ liệu
//// DELETE: xóa dữ liệu
//
///*
//1. Chuyển 1 java class sang dạng restfullAPI web service để springboot hiểu đây không phải class thường
//2. Khác với @Controller cần phải thêm @ResponseBody để output(dạng Map, Beans) được chuyển về json(để bên front-end hiểu).
//@RestController tích hợp @ResponseBody nên không cần thêm annotation này
//*/
//
//@RestController
//public class BuildingAPI {
//    // convert kiểu bean sang json để front-end hiểu. Thêm annotation ResponseBody ==================================
//    @GetMapping(value="/api/building1/")
//    public BuildingDTO getBuilding(@RequestParam(value = "name") String nameBuilding,
//                                   @RequestParam(value = "numberOfBasement", required = false) Integer numberOfBasement,
//                                   @RequestParam(value = "ward", required = false) String ward) {
//
//        BuildingDTO buildingDTO = new BuildingDTO();
//        buildingDTO.setName(nameBuilding);
//        buildingDTO.setNumOfBasement(numberOfBasement);
//        buildingDTO.setWard(ward);
//        return buildingDTO;
//    }
//
//    // thay vì xử lý từng params ta sử dụng RequestBody để truyền tất cả params. Với params ở dạng MAP =============
//    @PostMapping(value="/api/building2/")
//    public Map<String, String> getBuilding2(@RequestBody Map<String, String> params) {
//        return params;
//    }
//
//    // thay MAP bằng kiểu beans tự định nghĩa =====================================================================
//    @PostMapping(value="/api/building3/")
//    public BuildingDTO getBuilding2(@RequestBody BuildingDTO buildingDTO) {
//        BuildingDTO buildingDTO2 = new BuildingDTO();
//        buildingDTO2.setName(buildingDTO.getName());
//        buildingDTO2.setNumOfBasement(buildingDTO.getNumOfBasement());
//        buildingDTO2.setWard(buildingDTO.getWard());
//        return buildingDTO2;
//    }
//
//    // path parameter, truyền tham số vào path =====================================================================
//    @DeleteMapping("/api/building4/{id}/{name}")
//    public void  deleteBuilding(@PathVariable Integer id,
//                                @PathVariable String name) {
//        System.out.println("Deleted Building has id: " + id + " with name: " + name);
//    }
//
//    // Tạo 1 đối tượng custom error mới ==========================================================================
//    @GetMapping(value="/api/building5/")
//    public Object getBuilding3(@RequestParam(value = "name") String nameBuilding,
//                                @RequestParam(value = "numOfBasement", required = false) Integer numberOfBasement,
//                                @RequestParam(value = "ward", required = false) String ward) {
//
//
//        try {
//            int num = 5/0;
//        }
//        catch (Exception e) {
//            ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
//            errorResponseDTO.setError(e.getMessage());
//
//            List<String> details = new ArrayList<>();
//            details.add("So nguyen khong chia duoc cho 0");
//            errorResponseDTO.setDetails(details);
//            return errorResponseDTO;
//        }
//
//        BuildingDTO buildingDTO = new BuildingDTO();
//        buildingDTO.setName(nameBuilding);
//        buildingDTO.setNumOfBasement(numberOfBasement);
//        buildingDTO.setWard(ward);
//        return buildingDTO;
//    }
//
//    // Tạo 1 custom exception ==============================================================================
//    // nếu FieldRequiredException extends từ Exception thì validate cần throw FieldRequiredException
//    // còn extends từ RuntimeException thi không cần throw
//    public void validateParams(BuildingDTO buildingDTO) {
//        String name = buildingDTO.getName();
//        Integer numOfBasement = buildingDTO.getNumOfBasement();
//        if (name == null || name.equals("") || numOfBasement == null || numOfBasement == 0) {
//            throw new FieldRequiredException("Name or Num of basement is null");
//        }
//    }
//
//    @PostMapping(value="/api/building6/")
//    public Object getBuilding4(@RequestBody BuildingDTO buildingDTO) {
//        try {
//            validateParams(buildingDTO);
//        }
//        catch (FieldRequiredException e) {
//            ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
//            errorResponseDTO.setError(e.getMessage());
//
//            List<String> details = new ArrayList<>();
//            details.add("Kiem tra lai name hoac num of basement");
//            errorResponseDTO.setDetails(details);
//            return errorResponseDTO;
//        }
//        return buildingDTO;
//    }
//
//    @PostMapping(value="/api/building7/")
//    public Object getBuilding5(@RequestBody BuildingDTO buildingDTO) {
////        System.out.println(5/0);
//        validateParams(buildingDTO);
//        return buildingDTO;
//    }
//}
//
//
